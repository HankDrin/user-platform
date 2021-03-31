package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/30.
 */
public class ServletContextConfigSource extends MapBasedConfigSource {

    private final ServletContext servletContext;

    public ServletContextConfigSource(ServletContext servletContext) {
        super(String.format("ServletContext[path:%s] Init Parameters", servletContext.getContextPath()), 500,
                fetchConfigDate(servletContext));
        this.servletContext = servletContext;
    }

    private static Map<String, String> fetchConfigDate(ServletContext servletContext) {
        Map<String, String> configData = new HashMap<>();
        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName, servletContext.getInitParameter(parameterName));
        }
        return configData;
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        configData.putAll(fetchConfigDate(servletContext));
    }
}
