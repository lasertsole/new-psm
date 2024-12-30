package com.psm.infrastructure.Cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.psm.infrastructure.Cache.decorator.MultiLevelCacheConfig;
import com.psm.infrastructure.Cache.decorator.MultiLevelCacheManager;
import com.psm.infrastructure.Cache.decorator.MultiLevelChannel;
import com.psm.infrastructure.Cache.properties.CaffeineProperties;
import com.psm.infrastructure.Cache.properties.RedisProperties;
import com.psm.infrastructure.Cache.properties.RedissionProperties;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.context.annotation.Configuration;
import com.psm.infrastructure.Cache.utils.FastJson2RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Setter
@Configuration
@EnableCaching
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class CacheConfig {
    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private RedissionProperties redissionProperties;

    @Autowired
    private CaffeineProperties caffeineProperties;

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
            .expireAfterWrite(caffeineProperties.getExpireAfterWrite(), TimeUnit.MINUTES)
            .initialCapacity(caffeineProperties.getInitialCapacity())
            .maximumSize(caffeineProperties.getMaximumSize())
            .weakKeys().recordStats();
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://"+redisProperties.getHost()+":"+redisProperties.getPort());
        singleServerConfig.setPassword(redisProperties.getPassword());
        singleServerConfig.setConnectionPoolSize(redissionProperties.getConnectionPoolSize()); // 每个节点的连接池大小
        singleServerConfig.setConnectionMinimumIdleSize(redissionProperties.getConnectionMinimumIdleSize()); // 可选：设置最小空闲连接数
        config.setThreads(redissionProperties.getThreads()); // Redisson 内部线程数
        return Redisson.create(config);
    }

    /**
     * spring-cache缓存管理器
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient, Caffeine caffeine) {
        MultiLevelChannel multiLevelChannel = new MultiLevelChannel(redissonClient, caffeine);
        Map<String, MultiLevelCacheConfig> config = new ConcurrentHashMap<>();

        config.put("loginUserCache", new MultiLevelCacheConfig(60 * 60 * 1000, 60 * 60 * 1000, 1000));
        config.put("userCache", new MultiLevelCacheConfig(60 * 60 * 1000, 60 * 60 * 1000, 1000));
        config.put("models_UserCache", new MultiLevelCacheConfig(60 * 60 * 1000, 60 * 60 * 1000, 500));
        config.put("socketRoomCache", new MultiLevelCacheConfig(60 * 60 * 1000, 30 * 60 * 1000, 50));

        return new MultiLevelCacheManager(multiLevelChannel, config);
    }

    @Autowired
    private ApplicationContext applicationContext;
    @PreDestroy
    public void destroy() throws IOException {
        // Caffeine不需要显式销毁

        Map<String, RedissonClient> redissonClients = applicationContext.getBeansOfType(RedissonClient.class);
        for (Map.Entry<String, RedissonClient> entry : redissonClients.entrySet()) {
            log.info("关闭Redisson客户端: {}", entry.getKey());
            entry.getValue().shutdown();
        }
    }
}
