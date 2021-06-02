package org.geektimes.projects.spring.cloud.config.client;

import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author C.HD
 * @since 1.0
 */
public class FilePropertySourceLocator implements PropertySourceLocator {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String configFileLocation = "META-INF/config/default.properties";

    @Override
    public PropertySource<?> locate(Environment environment) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(configFileLocation);
        if (resource == null) {
            return null;
        }
        try (InputStream inputStream = resource.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return new PropertiesPropertySource("myPropertiesFile", properties);
        } catch (IOException e) {
            return null;
        }
    }
}
