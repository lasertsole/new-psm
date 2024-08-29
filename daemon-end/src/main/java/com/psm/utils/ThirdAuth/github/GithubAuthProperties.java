package com.psm.utils.ThirdAuth.github;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.github")
public class GithubAuthProperties {
    private String clientId;
    private String clientSecret;
    private String directUrl;

    /**
     * Github认证令牌服务器地址
     */
    private final String accessTokenUrl = "https://github.com/login/oauth/access_token";

    /**
     * Github认证服务器地址
     */
    private final String authorizeUrl = "https://github.com/login/oauth/authorize";

    /**
     * Github资源服务器地址
     */
    private final String resourceUrl = "https://api.github.com/user";
}
