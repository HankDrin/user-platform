package org.geektimes.configuration.microprofile.config.servlet.listener;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/31.
 */
public class ConfigServletRequestListener implements ServletRequestListener {

    private static final ThreadLocal<Config> configThreadLocal = new ThreadLocal<>();

    public static Config getConfig() {
        return configThreadLocal.get();
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest servletRequest = sre.getServletRequest();
        ServletContext servletContext = servletRequest.getServletContext();
        ClassLoader classLoader = servletContext.getClassLoader();
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        Config config = configProviderResolver.getConfig(classLoader);
        configThreadLocal.set(config);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        configThreadLocal.remove();
    }
}
