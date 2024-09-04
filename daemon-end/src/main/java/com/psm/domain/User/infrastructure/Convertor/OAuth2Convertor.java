package com.psm.domain.User.infrastructure.Convertor;

import com.psm.domain.User.entity.OAuth2User.OAuth2ThirdAccount;
import com.psm.domain.User.entity.User.UserDAO;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class OAuth2Convertor {
    public static OAuth2ThirdAccount giteeConvertToOAuth2ThirdAccount(String registerationId, OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        //创建本地应用的账户对象
        OAuth2ThirdAccount account = new OAuth2ThirdAccount();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        account.setProviderUserId(attributes.get("id").toString());
        account.setName((String) attributes.get("name"));
        account.setAvatar((String) attributes.get("avatar_url"));
        account.setRegistrationId(registerationId);

        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        account.setCredentials(accessToken.getTokenValue());
        // expiresAt默认采用ISO 8601标准时间格式(使用零时区)
        ZonedDateTime zonedDateTime = accessToken.getExpiresAt().atZone(ZoneId.systemDefault());
        account.setCredentialsExpiresAt(Date.from(zonedDateTime.toInstant()));

        // 返回
        return account;
    }

    public static UserDAO oAuth2ThirdAccountConvertToUserDAO(OAuth2ThirdAccount oAuth2ThirdAccount){
        UserDAO userDAO = new UserDAO();

        System.out.println(oAuth2ThirdAccount);

        return userDAO;
    }
}
