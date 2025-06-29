package com.psm.utils.Redis;

import com.psm.utils.Entity.EntityUtils;
import com.psm.utils.json.JacksonUtils;
import com.psm.utils.spring.SpringUtils;
import com.psm.utils.Boolean.BooleanUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class RedisAsyncUtils {
    private static final StringRedisTemplate STRING_REDIS_TEMPLATE = SpringUtils.getBean(StringRedisTemplate.class);
    private static final ValueOperations<String, String> OPS_FOR_VALUE = STRING_REDIS_TEMPLATE.opsForValue();

    /**
     * 将普通对象先转换成JSON字符串 然后转换成字节数组
     *
     * @param value 字符串对象
     * @return 字节数组
     */
    private static <T> byte[] toByte(T value) {
        Objects.requireNonNull(value);
        return JacksonUtils.writeValue(value).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 将字节数组先转换成JSON字符串 然后转换成对象
     *
     * @param bytes 字节数组
     * @param clazz Class对象
     * @return 对象
     */
    private static <T> T toObj(byte[] bytes, Class<T> clazz) {
        Objects.requireNonNull(bytes);
        return JacksonUtils.readObjectValue(new String(bytes), clazz);
    }

    /**
     * 以异步的方式 将Value对象保存到Redis中 无过期时间限制
     *
     * @param key   Key
     * @param value 实体对象
     * @param <T>   实体类型
     */
    public static <T> void save(String key, T value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        String jsonValue = JacksonUtils.writeValue(value);
        STRING_REDIS_TEMPLATE.execute((RedisCallback<Boolean>) conn -> conn.set(toByte(key), toByte(jsonValue)));
    }

    /**
     * 使用异步的方式 通过Key从Redis中取出Value对象
     *
     * @param key   Key
     * @param clazz Value值Class对象
     * @param <T>   Value值类型
     * @return Value值
     */
    public static <T> T getObject(String key, Class<T> clazz) {
        Objects.requireNonNull(key);
        return STRING_REDIS_TEMPLATE.execute((RedisCallback<T>) conn -> toObj(conn.get(toByte(key)), clazz));
    }

    /**
     * 批量设置Key过期时间
     */
    public static Boolean expire(Collection<String> keys, long sec) {
        STRING_REDIS_TEMPLATE.execute((RedisCallback<Boolean>) conn -> {
            List<Boolean> bool = new ArrayList<>();
            keys.forEach(e -> bool.add(conn.expire(toByte(e), sec)));
            return BooleanUtils.isTrue(bool);
        });
        return false;
    }

    /**
     * 批量设置Key过期时间
     */
    public static Boolean pExpire(Collection<String> keys, long millis) {
        return STRING_REDIS_TEMPLATE.execute((RedisCallback<Boolean>) conn -> {
            List<Boolean> bool = new ArrayList<>();
            keys.forEach(e -> bool.add(conn.pExpire(toByte(e), millis)));
            return BooleanUtils.isTrue(bool);
        });
    }

    /**
     * 批量删除Key
     */
    public static Long remove(Collection<String> keys) {
        byte[][] bytes = EntityUtils.toArray(keys, RedisAsyncUtils::toByte);
        return Optional.ofNullable(bytes).map(e -> STRING_REDIS_TEMPLATE.execute((RedisCallback<Long>) conn -> conn.del(e))).orElse(0L);
    }
}
