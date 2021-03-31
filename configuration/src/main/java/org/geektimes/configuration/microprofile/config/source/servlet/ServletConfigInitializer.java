package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.servlet.listener.ConfigServletRequestListener;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/30.
 */
public class ServletConfigInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        ctx.addListener(ServletContextConfigInitializer.class);
        ctx.addListener(ConfigServletRequestListener.class);
    }
}
