package com.psm.domain.IndependentDomain.User.user.entity.OAuth2ThirdAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2ThirdAccountDTO implements Serializable {

    private Long id;                 //自增id

    private Long userId;             //外键用户id

    private String registrationId;      //第三方登录类型id

    private String providerUserId;               //第三方登录账号id

    private String name;                //第三方登录用户名
    private String avatar;              //第三方用户头像

    private String Credentials;         //第三方用户凭证

    private Date credentialsExpiresAt;  //第三方用户凭证过期时间
}
