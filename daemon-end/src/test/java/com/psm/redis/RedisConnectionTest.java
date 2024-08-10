package com.psm.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testRedisConnection() {
        String key = "testKey";
        String value = "Hello, Redis!";

        // 尝试写入数据
        redisTemplate.opsForValue().set(key, value);

        // 读取数据
        String result = redisTemplate.opsForValue().get(key);

        // 断言结果
        assertEquals(value, result, "Redis connection failed.");
    }
}