package com.psm.domain.User.entity.OAuth2;

import java.io.Serializable;
import java.util.Set;

public class ClientRegistration implements Serializable {
    // 应用id
    private String clientId;

    // 应用密钥
    private String clientSecret;

    // 回调地址
    private String redirectUri;

    // 授权类型
    private Set<String> scope;


}
