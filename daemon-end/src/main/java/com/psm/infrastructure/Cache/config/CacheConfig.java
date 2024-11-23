package com.psm.infrastructure.Cache.config;

import org.springframework.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.psm.infrastructure.Cache.decorator.MultiLevelCacheConfig;
import com.psm.infrastructure.Cache.decorator.MultiLevelCacheManager;
import com.psm.infrastructure.Cache.decorator.MultiLevelChannel;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.context.annotation.Configuration;
import com.psm.infrastructure.Cache.utils.FastJson2RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class CacheConfig {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJson2RedisSerializer serializer = new FastJson2RedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public Caffeine<Object, Object> caffeine() {
    	return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(1000)
                .weakKeys().recordStats();
    }

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://"+redisHost+":"+redisPort);
        singleServerConfig.setPassword(redisPassword);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    /**
     * spring-cache缓存管理器
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient, Caffeine caffeine) {
        MultiLevelChannel multiLevelChannel = new MultiLevelChannel(redissonClient, caffeine);
        Map<String, MultiLevelCacheConfig> config = new ConcurrentHashMap<>();

        config.put("loginCache", new MultiLevelCacheConfig(60 * 60 * 1000,  60 * 60 * 1000, 1000));
        config.put("models_UserCache", new MultiLevelCacheConfig(60 * 60 * 1000,  60 * 60 * 1000, 500));
        config.put("model_ExtendedUserCache", new MultiLevelCacheConfig(30 * 60 * 1000,  5 * 60 * 1000, 200));

        return new MultiLevelCacheManager(multiLevelChannel, config);
    }
}
