package com.psm.infrastructure.Cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")//配置和jwt一样的过期时间
public class CacheProperties {
    /**
     * 默认有效期为
     */
    private Long expiration;//配置和jwt一样的过期时间,单位为毫秒
}
