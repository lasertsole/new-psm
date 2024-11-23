package com.psm.infrastructure.Cache.decorator;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class MultiLevelCacheManager implements CacheManager {
    private Map<String, MultiLevelCacheConfig> configMap;

    private ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();

    private MultiLevelChannel multiLevelChannel;

    public MultiLevelCacheManager(MultiLevelChannel multiLevelChannel, Map<String, MultiLevelCacheConfig> configMap) {
        this.multiLevelChannel = multiLevelChannel;
        this.configMap = configMap;
    }

    private RedissonCache createRedissonCache(RedissonClient redissonClient, String cacheName, MultiLevelCacheConfig multiLevelCacheConfig) {
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setTTL(multiLevelCacheConfig.getTTL());
        cacheConfig.setMaxIdleTime(multiLevelCacheConfig.getMaxIdleTime());
        cacheConfig.setMaxSize(multiLevelCacheConfig.getMaxSize());
        RMapCache<Object, Object> map = redissonClient.getMapCache(cacheName);
        return new RedissonCache(map, cacheConfig, true);
    }

    private synchronized MultiLevelCache createMultiLevelCache(String cacheName, MultiLevelCacheConfig config) {
        com.github.benmanes.caffeine.cache.Cache<Object, Object> localCache = multiLevelChannel.getCaffeine().build();
        RedissonCache redissonCache = createRedissonCache(multiLevelChannel.getRedissonClient(), cacheName, config);
        return new MultiLevelCache(cacheName, localCache, redissonCache);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        MultiLevelCacheConfig config = configMap.get(name);
        cache = createMultiLevelCache(name, config);
        instanceMap.put(name, cache);
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(configMap.keySet());
    }
}
