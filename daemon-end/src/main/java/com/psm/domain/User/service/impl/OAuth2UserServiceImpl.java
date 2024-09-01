package com.psm.domain.User.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.User.entity.OAuth2.OAuth2ThirdAccount;
import com.psm.domain.User.infrastructure.UserInfrastructure;
import com.psm.domain.User.repository.OAuth2ThirdAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    @Autowired
    private OAuth2ThirdAccountMapper mapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //转换 oAuth2User 为 OAuth2ThirdAccount
        String registerationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2ThirdAccount oAuth2ThirdAccount = null;
        if (registerationId.equals("github")) {
            //TODO github
        } else if (registerationId.equals("gitee")) {
            oAuth2ThirdAccount =
                    UserInfrastructure.ThirdPathInfoConvertToOAuth2ThirdAccount(registerationId, userRequest, oAuth2User);
            System.out.println(oAuth2ThirdAccount);
        }

        // 判断是否已注册，如果已注册，则存入数据库
        if (!Objects.isNull(oAuth2ThirdAccount)){
            LambdaQueryWrapper<OAuth2ThirdAccount> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OAuth2ThirdAccount::getUserId, oAuth2ThirdAccount.getUserId());
            OAuth2ThirdAccount oAuth2ThirdAccount1 = mapper.selectOne(wrapper);

            // 添加或更新改用户已在数据库内的信息
            mapper.insertOrUpdate(oAuth2ThirdAccount);
        }

        // 返回用户信息
        return oAuth2User;
    }
}
