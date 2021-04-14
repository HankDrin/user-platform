package org.geektimes.cache.redis;

import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.serialization.SerializationStrategy;
import org.geektimes.cache.serialization.SerializationStrategyProvider;
import redis.clients.jedis.Jedis;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.*;
import java.util.Iterator;

public class JedisCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final Jedis jedis;

    private final SerializationStrategy serializationStrategy;

    public JedisCache(CacheManager cacheManager, String cacheName,
                      Configuration<K, V> configuration, Jedis jedis) {
        super(cacheManager, cacheName, configuration);
        this.jedis = jedis;
        serializationStrategy = new SerializationStrategyProvider(getClass().getClassLoader()).get();
    }

    @Override
    protected V doGet(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializationStrategy.serialize(key);
        return doGet(keyBytes);
    }

    protected V doGet(byte[] keyBytes) {
        byte[] valueBytes = jedis.get(keyBytes);
        if (valueBytes == null || valueBytes.length == 0) {
            return null;
        }
        return serializationStrategy.deserialize(valueBytes);
    }

    @Override
    protected V doPut(K key, V value) throws CacheException, ClassCastException {
        byte[] keyBytes = serializationStrategy.serialize(key);
        byte[] valueBytes = serializationStrategy.serialize(value);
        V oldValue = doGet(keyBytes);
        jedis.set(keyBytes, valueBytes);
        return oldValue;
    }

    @Override
    protected V doRemove(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializationStrategy.serialize(key);
        V oldValue = doGet(keyBytes);
        jedis.del(keyBytes);
        return oldValue;
    }

    @Override
    protected void doClear() throws CacheException {

    }

    @Override
    protected void doClose() {
        this.jedis.close();
    }

    @Override
    protected Iterator<Entry<K, V>> newIterator() {
        return null;
    }

}
