package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.ConfigValue;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/29.
 */
public class DefaultConfigValue implements ConfigValue {

    private String name;

    private String value;

    private String rawValue;

    private String sourceName;

    private int sourceOrdinal;

    public DefaultConfigValue(String name, String value, String rawValue, String sourceName, int sourceOrdinal) {
        this.name = name;
        this.value = value;
        this.rawValue = rawValue;
        this.sourceName = sourceName;
        this.sourceOrdinal = sourceOrdinal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getRawValue() {
        return rawValue;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public int getSourceOrdinal() {
        return sourceOrdinal;
    }
}
