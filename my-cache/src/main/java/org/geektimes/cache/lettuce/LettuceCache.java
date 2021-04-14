package org.geektimes.cache.lettuce;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.geektimes.cache.AbstractCache;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Iterator;

/**
 * TODO
 *
 * @author C.HD
 * @since 1.0
 */
public class LettuceCache <K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final RedisCommands redisCommands;

    private final StatefulRedisConnection connection;

    public LettuceCache(CacheManager cacheManager, String cacheName, Configuration<K, V> configuration, StatefulRedisConnection connection) {
        super(cacheManager, cacheName, configuration);
        this.connection = connection;
        this.redisCommands = connection.sync();
    }

    @Override
    protected V doGet(K key) throws CacheException, ClassCastException {
        Object value = redisCommands.get(key);
        if (value == null) {
            return null;
        }
        return (V) value;
    }

    @Override
    protected V doPut(K key, V value) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        redisCommands.set(key, value);
        return oldValue;
    }

    @Override
    protected V doRemove(K key) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        redisCommands.del(key);
        return oldValue;
    }

    @Override
    protected void doClear() throws CacheException {

    }

    @Override
    protected void doClose() {
        connection.close();
    }

    @Override
    protected Iterator<Entry<K, V>> newIterator() {
        return null;
    }
}
