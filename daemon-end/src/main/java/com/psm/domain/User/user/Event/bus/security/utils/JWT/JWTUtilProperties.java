package com.psm.domain.User.user.Event.bus.security.utils.JWT;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JWTUtilProperties {
    /**
     * 默认有效期为
     */
    public Long expiration;

    /**
     * 设置密钥
     */
    public String secret;

    /**
     * 签发者
     */
    public String issuer;
}
