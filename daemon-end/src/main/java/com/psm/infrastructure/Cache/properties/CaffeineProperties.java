package com.psm.infrastructure.Cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.caffeine")//配置和jwt一样的过期时间
public class CaffeineProperties {
    private int expireAfterWrite;
    private int initialCapacity;
    private int maximumSize;
}
