package com.psm.domain.User.infrastructure;

import com.psm.domain.User.entity.OAuth2.OAuth2ThirdAccount;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.entity.User.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class UserInfrastructure {
    public static OAuth2ThirdAccount ThirdPathInfoConvertToOAuth2ThirdAccount(String registerationId,OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        //创建本地应用的账户对象
        OAuth2ThirdAccount account = new OAuth2ThirdAccount();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        account.setUserId((Integer) attributes.get("id"));
        account.setLogin((String) attributes.get("login"));
        account.setName((String) attributes.get("name"));
        account.setAvatar((String) attributes.get("avatar_url"));
        account.setRegistrationId(registerationId);

        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        account.setCredentials(accessToken.getTokenValue());
        // expiresAt默认采用ISO 8601标准时间格式(使用零时区)
        ZonedDateTime zonedDateTime = accessToken.getExpiresAt().atZone(ZoneId.systemDefault());
        account.setCredentialsExpiresAt(Date.from(zonedDateTime.toInstant()));
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());

        // 返回
        return account;
    }

    public static UserDAO DTOConvertToDAO(UserDTO userDTO){
        UserDAO userDAO = new UserDAO();
        BeanUtils.copyProperties(userDTO, userDAO);

        return userDAO;
    }

    public static UserVO DAOConvertToVO(UserDAO userDAO){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDAO, userVO);

        return userVO;
    }
}
