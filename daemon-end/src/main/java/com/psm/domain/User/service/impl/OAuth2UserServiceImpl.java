package com.psm.domain.User.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.psm.domain.User.entity.OAuth2User.OAuth2ThirdAccount;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.infrastructure.Convertor.OAuth2Convertor;
import com.psm.domain.User.repository.OAuth2ThirdAccountMapper;
import com.psm.domain.User.repository.UserMapper;
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
    private OAuth2ThirdAccountMapper oauth2Mapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //转换 oAuth2User 为 OAuth2ThirdAccount
        String registerationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2ThirdAccount oAuth2ThirdAccount;
        if (registerationId.equals("github")) {
            oAuth2ThirdAccount = null;
            //TODO github
        } else if (registerationId.equals("gitee")) {
            oAuth2ThirdAccount =
                    OAuth2Convertor.giteeConvertToOAuth2ThirdAccount(registerationId, userRequest, oAuth2User);
        } else {
            oAuth2ThirdAccount = null;
        }

        // 判断是否用户是否在第三方平台有账号
        if (!Objects.isNull(oAuth2ThirdAccount)){
            LambdaQueryWrapper<OAuth2ThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OAuth2ThirdAccount::getRegistrationId, oAuth2ThirdAccount.getRegistrationId())
                    .and(w -> w.eq(OAuth2ThirdAccount::getProviderUserId, oAuth2ThirdAccount.getProviderUserId()));
            OAuth2ThirdAccount oAuth2ThirdAccount1 = oauth2Mapper.selectOne(queryWrapper);

            // 添加或更新改用户已在数据库内的信息
            if (Objects.isNull(oAuth2ThirdAccount1)){//判断用户的第三方平台账号是否已在数据库
                //在tb_user表中新建一个账号
                //TODO 将OAuth2ThirdAccount转化为UserDAO
                UserDAO userDAO = OAuth2Convertor.OAuth2ThirdAccountConvertToUserDAO(oAuth2ThirdAccount);

                //获取tb_user表新建账号的id

                //在tb_third_party_user表中新建第三方账号，外键user_id的值为tb_user表的id
                oauth2Mapper.insert(oAuth2ThirdAccount);
            }
            else{
                LambdaUpdateWrapper<OAuth2ThirdAccount> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(OAuth2ThirdAccount::getRegistrationId, oAuth2ThirdAccount.getRegistrationId())
                        .and(w -> w.eq(OAuth2ThirdAccount::getProviderUserId, oAuth2ThirdAccount.getProviderUserId()))
                        .set(!ObjectUtil.isEmpty(oAuth2ThirdAccount.getName()), OAuth2ThirdAccount::getName, oAuth2ThirdAccount.getName())
                        .set(!ObjectUtil.isEmpty(oAuth2ThirdAccount.getAvatar()), OAuth2ThirdAccount::getAvatar, oAuth2ThirdAccount.getAvatar())
                        .set(!ObjectUtil.isEmpty(oAuth2ThirdAccount.getCredentials()), OAuth2ThirdAccount::getCredentials, oAuth2ThirdAccount.getCredentials())
                        .set(!ObjectUtil.isEmpty(oAuth2ThirdAccount.getCredentialsExpiresAt()), OAuth2ThirdAccount::getCredentialsExpiresAt, oAuth2ThirdAccount.getCredentialsExpiresAt());

                oauth2Mapper.update(null, updateWrapper);
            }
        }

        // 返回用户信息
        return oAuth2User;
    }
}
