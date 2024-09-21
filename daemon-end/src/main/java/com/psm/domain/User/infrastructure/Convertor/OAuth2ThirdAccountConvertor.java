package com.psm.domain.User.infrastructure.Convertor;

import com.psm.domain.User.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;
import com.psm.domain.User.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDTO;
import com.psm.domain.User.entity.User.UserDAO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Mapper
public abstract class OAuth2ThirdAccountConvertor {
    public static final OAuth2ThirdAccountConvertor INSTANCE = Mappers.getMapper(OAuth2ThirdAccountConvertor.class);
    public OAuth2ThirdAccountDTO gitee2OAuthThirdAccount(String registerationId, OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        //创建本地应用的账户对象
        OAuth2ThirdAccountDTO account = new OAuth2ThirdAccountDTO();

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

    @Mappings(
            @Mapping(source = "userId", target = "id")
    )
    public abstract UserDAO DTO2UserDAO(OAuth2ThirdAccountDTO oAuth2ThirdAccount);

    public abstract OAuth2ThirdAccountDAO DTO2DAO(OAuth2ThirdAccountDTO oAuth2ThirdAccountDTO);
}
