package com.psm.infrastructure.Cache.decorator;

import com.psm.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Cache装饰器模式
 */
@Slf4j
public class MultiLevelCache implements Cache {
    // 广播主题
    private final RTopic topic;

    // 一级缓存caffeine
    private final com.github.benmanes.caffeine.cache.Cache localCache;

    // 二级缓存redis
    private final RedissonCache redissonCache;

    // 缓存名称
    private String cacheName;

    public MultiLevelCache(String cacheName, com.github.benmanes.caffeine.cache.Cache localCache, RedissonCache redissonCache) {
        this.cacheName = cacheName;
        this.localCache = localCache;
        this.redissonCache = redissonCache;

        RedissonClient redissonClient = (RedissonClient) SpringUtils.getBean("redissonClient");
        this.topic = redissonClient.getTopic(cacheName);
        this.topic.addListener(List.class, (channel, msg) -> {
            forwardPut(msg.get(0), msg.get(1));
        });
    }
    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this.redissonCache.getNativeCache();
    }

    public Object getRawResult(Object key) {
        Object result = localCache.getIfPresent(key);
        if (result != null) {
            return result;
        }
        result = redissonCache.getNativeCache().get(key);
        if (result != null) {
            localCache.put(key, result);
        }
        return result;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object result = getRawResult(key);
        if (result == null) {
            return null;
        }
        return new SimpleValueWrapper(result);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object rawResult = getRawResult(key);
        if (rawResult != null && type.isInstance(rawResult)) {
            return type.cast(rawResult);
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        try {
            Object result = getRawResult(key);
            if (result != null) {
                return (T) result;
            }
            T loadedValue = valueLoader.call();
            localCache.put(key, loadedValue);

            return loadedValue;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void put(Object key, Object value) {
        this.topic.publish(List.of(key, value));//广播要存入的信息
        localCache.put(key, value);
        redissonCache.put(key, value);
    }

    // 分布式缓存，只更新本地缓存
    public void forwardPut(Object key, Object value) {
        localCache.put(key, value);
    }


    @Override
    public void evict(Object key) {
        localCache.invalidate(key);
        redissonCache.getNativeCache().remove(key);
    }

    @Override
    public void clear() {
        localCache.invalidateAll();
        redissonCache.clear();
    }
}
