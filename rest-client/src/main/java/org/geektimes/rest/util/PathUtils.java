package org.geektimes.rest.util;

import javax.ws.rs.Path;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Path Utilities Class
 *
 * @author C.HD
 * @since 2021/4/6.
 */
public interface PathUtils {

    String SLASH = "/";

    char SLASH_CHAR = SLASH.charAt(0);

    static String resolvePath(Class<?> resourceClass, Method handleMethod) {
        String pathFromResourceClass = resolvePath(resourceClass);
        String pathFromHandleMethod = resolvePath(handleMethod);
        return pathFromResourceClass != null ? pathFromResourceClass + pathFromHandleMethod : pathFromHandleMethod;
    }

    static String resolvePath(AnnotatedElement annotatedElement) {
        Path path = annotatedElement.getAnnotation(Path.class);
        if (path == null) {
            return null;
        }

        String value = path.value();
        if (!value.startsWith(SLASH)) {
            value = SLASH + value;
        }
        return value;
    }

    static String resolvePath(Class resource, String methodName) {
        return Arrays.stream(resource.getDeclaredMethods())
                     .filter(m -> Objects.equals(m.getName(), methodName))
                     .map(PathUtils::resolvePath)
                     .filter(Objects::nonNull)
                     .findFirst()
                     .get();
    }

}
