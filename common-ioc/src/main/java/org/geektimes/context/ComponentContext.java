package org.geektimes.context;

import org.geektimes.function.ThrowableAction;
import org.geektimes.function.ThrowableFunction;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContext;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * 组件上下文 (Web 应用全局使用)
 *
 * @author C.HD
 * @since 2021/3/10.
 */
public class ComponentContext {

    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    private static final Logger logger = Logger.getLogger(CONTEXT_NAME);

    private static ServletContext servletContext;

    private Context envContext;

    private ClassLoader classLoader;

    private Map<String, Object> componentsMap = new LinkedHashMap<>();

    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    public void init(ServletContext servletContext) throws RuntimeException {
        ComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);
        // 获取当前 ServletContext（WebApp）ClassLoader
        this.classLoader = servletContext.getClassLoader();
        initEnvContext();
        instantiateComponents();
        initializeComponents();
    }

    public void destroy() throws RuntimeException {
        // 销毁阶段 - {@link PreDestroy}
        componentsMap.values().forEach(component -> {
            Class<?> componentClass = component.getClass();
            processPreDestroy(component, componentClass);
        });
        close(this.envContext);
    }

    /**
     * 通过名称进行依赖查找
     *
     * @param name
     * @param <C>
     * @return
     */
    public <C> C getComponent(String name) {
        return (C) componentsMap.get(name);
    }

    private void initializeComponents() {
        componentsMap.values().forEach(component -> {
            Class<?> componentClass = component.getClass();
            // 注入阶段 - {@link Resource}
            injectComponents(component, componentClass);
            // 初始化阶段 - {@link PostConstruct}
            processPostConstruct(component, componentClass);
        });
    }

    private void processPostConstruct(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods())
              .filter(method -> !Modifier.isStatic(
                      method.getModifiers()) && method.getParameterCount() == 0 && method.isAnnotationPresent(
                      PostConstruct.class))
              .forEach(method -> {
                  try {
                      method.invoke(component);
                  } catch (Exception e) {
                      throw new RuntimeException(e);
                  }
              });

    }

    private void processPreDestroy(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods())
              .filter(method -> !Modifier.isStatic(
                      method.getModifiers()) && method.getParameterCount() == 0 && method.isAnnotationPresent(
                      PreDestroy.class))
              .forEach(method -> {
                  try {
                      method.invoke(component);
                  } catch (Exception e) {
                      logger.warning(e.getMessage());
                  }
              });
    }

    public void injectComponents(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
              .filter(field -> {
                  int mods = field.getModifiers();
                  return !Modifier.isStatic(mods) && field.isAnnotationPresent(Resource.class);
              })
              .forEach(field -> {
                  Resource resource = field.getAnnotation(Resource.class);
                  String resourceName = resource.name();
                  Object injectedObject = lookupComponent(resourceName);
                  field.setAccessible(true);
                  try {
                      field.set(component, injectedObject);
                  } catch (IllegalAccessException e) {
                  }
              });
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function ThrowableFunction
     * @param <R>      返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function         ThrowableFunction
     * @param ignoredException 是否忽略异常
     * @param <R>              返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    protected <C> C lookupComponent(String name) {
        return executeInContext(context -> (C) context.lookup(name));
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function, boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Exception e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 实例化组件
     */
    private void instantiateComponents() {
        // 遍历获取所有组件的名称
        List<String> componentNames = listAllComponentNames();
        // 通过依赖查找，实例化对象（ Tomcat BeanFactory setter 方法的执行，仅支持简单类型）
        componentNames.forEach(name -> componentsMap.put(name, lookupComponent(name)));
    }

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    private List<String> listComponentNames(String name) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(name), true);

            if (e == null) {
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.next();
                String className = element.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    String fullName = name.startsWith("/") ? element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    private void initEnvContext() {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }


    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }

}
