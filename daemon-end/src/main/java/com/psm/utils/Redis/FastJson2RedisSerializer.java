package com.psm.utils.Redis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter.Feature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

    /**
     * cls.
     */
    private final Class<T> clazz;

    /**
     * 序列化.
     *
     * @param t object to serialize. Can be {@literal null}.
     * @return es
     * @throws SerializationException e
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        Map.Entry<String, T> entity = new SimpleEntry<>(t.getClass().getName(), t);
        return JSON.toJSONString(entity, Feature.WriteClassName).getBytes(Charset.defaultCharset());
    }

    /**
     * 反序列化.
     *
     * @param bytes object binary representation. Can be {@literal null}.
     * @return es
     * @throws SerializationException e
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        String str = new String(bytes, Charset.defaultCharset());
        int index = str.indexOf(":");
        String cls = str.substring(2, index - 1);
        String obj = str.substring(index + 1, str.length() - 1);
        return JSON.parseObject(
                obj,
                clazz,
                JSONReader.autoTypeFilter(
                        cls
                ),
                JSONReader.Feature.SupportClassForName);
    }

    /**
     * 构造器.
     *
     * @param clazz cls
     */
    public FastJson2RedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }
}