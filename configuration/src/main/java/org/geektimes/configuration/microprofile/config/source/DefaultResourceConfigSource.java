package org.geektimes.configuration.microprofile.config.source;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/29.
 */
public class DefaultResourceConfigSource extends MapBasedConfigSource {

    private static final String configFileLocation = "META-INF/microprofile-config.properties";

    public DefaultResourceConfigSource() {
        super("Default Config File", 100);
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<URL> resources = classLoader.getResources(configFileLocation);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            Properties properties = new Properties();
            try (InputStream inputStream = resource.openStream()) {
                properties.load(inputStream);
                configData.putAll(properties);
            }
        }
    }
}
