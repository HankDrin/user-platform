package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 基于 Map 数据结构 {@link ConfigSource} 实现
 *
 * @author C.HD
 * @since 2021/3/24.
 */
public abstract class MapBasedConfigSource implements ConfigSource {

    private final String name;

    private final int ordinal;

    private final Map<String, String> source;

    public MapBasedConfigSource(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
        this.source = getProperties();
    }

    public MapBasedConfigSource(String name, int ordinal, Map<String, String> source) {
        this.name = name;
        this.ordinal = ordinal;
        this.source = source;
    }

    /**
     * 获取配置数据 Map
     *
     * @return 不可变 Map 类型的数据配置
     */
    public final Map<String, String> getProperties() {
        Map<String, String> configData = new HashMap<>();
        try {
            prepareConfigData(configData);
        } catch (Throwable e) {
            throw new IllegalStateException("准备配置数据异常", e);
        }
        return Collections.unmodifiableMap(configData);
    }

    protected abstract void prepareConfigData(Map configData) throws Throwable;

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final int getOrdinal() {
        return ordinal;
    }

    @Override
    public Set<String> getPropertyNames() {
        return source.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return source.get(propertyName);
    }
}
