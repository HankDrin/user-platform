package org.geektimes.configuration.microprofile.config.source;

import java.util.Map;

/**
 * 操作系统环境变量 ConfigSource
 *
 * @author C.HD
 * @since 2021/3/24.
 */
public class OperationSystemEnvironmentVariablesConfigSource extends MapBasedConfigSource {

    public OperationSystemEnvironmentVariablesConfigSource() {
        super("Operation System Environment Variables", 300);
    }

    @Override
    protected void prepareConfigData(Map configData) {
        configData.putAll(System.getenv());
    }
}
