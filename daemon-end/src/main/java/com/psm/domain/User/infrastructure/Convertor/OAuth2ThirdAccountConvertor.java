package com.psm.domain.User.infrastructure.Convertor;

import cn.hutool.core.bean.BeanUtil;
import com.psm.domain.User.entity.LoginUser.LoginUser;
import com.psm.domain.User.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;
import com.psm.domain.User.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDTO;
import com.psm.domain.User.entity.User.UserDAO;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class OAuth2ThirdAccountConvertor {
    public static OAuth2ThirdAccountDTO giteeConvertToOAuth2ThirdAccount(String registerationId, OAuth2UserRequest userRequest, OAuth2User oAuth2User){
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

    public static UserDAO DTOConvertToUserDAO(OAuth2ThirdAccountDTO oAuth2ThirdAccount){
        UserDAO userDAO = new UserDAO();

        userDAO.setName(oAuth2ThirdAccount.getName());
        userDAO.setAvatar(oAuth2ThirdAccount.getAvatar());

        return userDAO;
    }

    public static LoginUser DTOConvertToLoginUser(OAuth2ThirdAccountDTO oAuth2ThirdAccount){
        UserDAO userDAO = DTOConvertToUserDAO(oAuth2ThirdAccount);

        return new LoginUser(userDAO);
    }

    public static OAuth2ThirdAccountDAO DTOConvertToDAO(OAuth2ThirdAccountDTO oAuth2ThirdAccountDTO){
        OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO = new OAuth2ThirdAccountDAO();

        BeanUtil.copyProperties(oAuth2ThirdAccountDTO, oAuth2ThirdAccountDAO);

        return oAuth2ThirdAccountDAO;
    }
}
