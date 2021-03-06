package org.geektimes.cache.integration;

import org.geektimes.cache.serialization.SerializationStrategy;
import org.geektimes.cache.serialization.SerializationStrategyProvider;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract {@link FallbackStorage} implementation.
 *
 * @author C.HD
 * @since 1.0
 */
public abstract class AbstractFallbackStorage<K, V> implements FallbackStorage<K, V> {

    private final int priority;

    protected AbstractFallbackStorage(int priority) {
        this.priority = priority;
    }

    @Override
    public Map<K, V> loadAll(Iterable<? extends K> keys) throws CacheLoaderException {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        for (K key : keys) {
            map.put(key, load(key));
        }
        return map;
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends K, ? extends V>> entries) throws CacheWriterException {
        entries.forEach(this::write);
    }

    @Override
    public void deleteAll(Collection<?> keys) throws CacheWriterException {
        keys.forEach(this::delete);
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
