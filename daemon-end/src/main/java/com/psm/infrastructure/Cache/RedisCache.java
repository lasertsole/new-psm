package com.psm.infrastructure.Cache;

import com.psm.infrastructure.Cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisCache {
    // 导入redis缓存配置
    @Autowired
    private CacheProperties redisCacheProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 获得缓存的基本对象并更新缓存时间。
     *
     * @param key 缓存键值
     * @param timeout 过期时间
     * @param timeUnit 时间颗粒度
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key, final Integer timeout, final TimeUnit timeUnit)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.getAndExpire(key, timeout, timeUnit);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final Collection<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()){
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 向Set中添加元素
     *
     * @param key 缓存的键值
     * @param member 集合成员对象
     */
    public <T> void cacheSetMember(final String key, final T member) {
        redisTemplate.opsForSet().add(key, member);
    }

    /**
     * 向Set中添加元素,并设置过期时间
     *
     * @param key 缓存的键值
     * @param member 集合成员对象
     * @param timeout 过期时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void cacheSetMember(final String key, final T member, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForSet().add(key, member);
        setExpire(key, timeout, timeUnit);
    }


    /**
     * 向Set中移除元素，如果删除后集合为空，则删除集合
     *
     * @param key 缓存的键值
     * @param member 集合成员对象
     */
    public <T> void deleteSetMember(final String key, final T member) {
        Long remove = redisTemplate.opsForSet().remove(key, member);
        Set<String> members = redisTemplate.opsForSet().members(key);
        // 如果set为空，则删除集合
        if (members == null || members.isEmpty())
        {
            deleteObject(key);
        }
    }

    /**
     * 向Set中移除元素，如果删除后集合为空，则删除集合。若不为空，则修改过期时间
     *
     * @param key 缓存的键值
     * @param member 集合成员对象
     * @param timeout 过期时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void deleteSetMember(final String key, final T member, final Integer timeout, final TimeUnit timeUnit) {
        Long remove = redisTemplate.opsForSet().remove(key, member);
        Set<String> members = redisTemplate.opsForSet().members(key);
        // 如果set为空，则删除集合
        if (members == null || members.isEmpty()) {
            deleteObject(key);
        } else {
            setExpire(key, timeout, timeUnit);
        }
    }

    public <T> void deleteSet(final String key) {
        deleteObject(key);
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return T类型对象
     */
    public <T> Set<T> getCacheSet(final String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获得缓存的set,并修改过期时间
     *
     * @param key
     * @param timeout 过期时间
     * @param timeUnit 时间颗粒度
     * @return
     */
    public <T> Set<T> getCacheSet(final String key, final Integer timeout, final TimeUnit timeUnit){
        setExpire(key, timeout, timeUnit);
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap){
        if (dataMap != null){
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value){
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey){
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key
     * @param hKey
     */
    public void deleteCacheMapValue(final String key, final String hKey){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys){
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 批量获取缓存的 list 对象
     *
     * @param keys 缓存的键值集合
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheLists(List<String> keys) {
        return (List<T>) keys.stream().map(this::getCacheObject).toList();
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public long getExpire(final String key){
        return getExpire(key, TimeUnit.SECONDS);
    }

    public long getExpire(final String key, final TimeUnit timeUnit){
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 设置过期时间
     *
     * @param key
     */
    public void setExpire(final String key){
        setExpire(key, redisCacheProperties.getExpiration()/1000);
    }

    public void setExpire(final String key, final long timeout){
        setExpire(key, timeout, TimeUnit.SECONDS);
    }

    public void setExpire(final String key, final long timeout, final TimeUnit timeUnit){
        redisTemplate.expire(key, timeout, timeUnit);
    }
}
