package org.geektimes.cache.event;

import javax.cache.configuration.Factory;
import javax.cache.event.*;

/**
 * TODO
 *
 * @author C.HD
 * @since 1.0
 */
public class TestCacheEntryListener<K, V> implements CacheEntryCreatedListener<K, V>, CacheEntryUpdatedListener<K, V>,
        CacheEntryExpiredListener<K, V>, CacheEntryRemovedListener<K, V>, Factory<CacheEntryListener<K, V>> {

    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
            throws CacheEntryListenerException {
        println("onCreated", events);
    }

    @Override
    public void onExpired(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
            throws CacheEntryListenerException {
        println("onExpired", events);
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
            throws CacheEntryListenerException {
        println("onRemoved", events);
    }

    @Override
    public void onUpdated(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
            throws CacheEntryListenerException {
        println("onUpdated", events);
    }

    private void println(String source, Iterable<CacheEntryEvent<? extends K, ? extends V>> cacheEntryEvents) {
        System.out.printf("[Thread : %s] %s - %s\n", Thread.currentThread().getName(), source, cacheEntryEvents);
    }

    @Override
    public CacheEntryListener<K, V> create() {
        return this;
    }
}
