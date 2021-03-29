package org.geektimes.configuration.microprofile.config.source;

import java.util.Map;

/**
 * Java 系统属性配置源
 *
 * @author C.HD
 * @since 2021/3/24.
 */
public class JavaSystemPropertiesConfigSource extends MapBasedConfigSource {

    public JavaSystemPropertiesConfigSource() {
        super("Java System Properties", 400);
    }

    @Override
    protected void prepareConfigData(Map configData) {
        configData.putAll(System.getProperties());
    }
}
