package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletConfig;
import java.util.Enumeration;
import java.util.Map;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/30.
 */
public class ServletConfigSource extends MapBasedConfigSource {

    private ServletConfig servletConfig;

    public ServletConfigSource(ServletConfig servletConfig) {
        super(String.format("Servlet[name:%s] Init Parameters", servletConfig.getServletName()), 600);
        this.servletConfig = servletConfig;
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        Enumeration<String> parameterNames = servletConfig.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName, servletConfig.getInitParameter(parameterName));
        }
    }
}
