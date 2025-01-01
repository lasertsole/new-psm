package com.psm.domain.Independent.User.Single.user.service.impl;

import com.psm.domain.Independent.User.Single.user.pojo.entity.LoginUser.LoginUser;
import com.psm.domain.Independent.User.Single.user.pojo.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDTO;
import com.psm.domain.Independent.User.Single.user.pojo.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDO;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserDO;
import com.psm.domain.Independent.User.Single.user.types.convertor.OAuth2ThirdAccountConvertor;
import com.psm.domain.Independent.User.Single.user.event.bus.security.utils.Oauth2UserIdContextHolder;
import com.psm.infrastructure.RepositoryImpl.User.user.OAuth2ThirdAccountDB;
import com.psm.infrastructure.RepositoryImpl.User.user.UserDB;
import com.psm.infrastructure.Cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OAuth2ThirdAccountServiceDetailImpl extends DefaultOAuth2UserService {
    @Autowired
    private OAuth2ThirdAccountDB oAuth2ThirdAccountDB;

    @Autowired
    private UserDB userDB;

    @Autowired
    private RedisCache redisCache;

    @Value("${spring.security.jwt.expiration}")
    public Long expiration;//jwt有效期

    @Override
    @Transactional//开启数据库事务
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //转换 oAuth2User 为 OAuth2ThirdAccount
        String registerationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2ThirdAccountDTO oAuth2ThirdAccountDTO;
        if (registerationId.equals("github")) {
            oAuth2ThirdAccountDTO = null;
            //TODO github
        } else if (registerationId.equals("gitee")) {
            oAuth2ThirdAccountDTO =
                    OAuth2ThirdAccountConvertor.INSTANCE.gitee2OAuthThirdAccount(registerationId, userRequest, oAuth2User);
        } else {
            oAuth2ThirdAccountDTO = null;
        }

        // 判断是否用户是否在第三方平台有账号
        if (!Objects.isNull(oAuth2ThirdAccountDTO)){
            //将DTO转换为DO
            OAuth2ThirdAccountDO oAuth2ThirdAccountDO = OAuth2ThirdAccountConvertor.INSTANCE.DTO2DO(oAuth2ThirdAccountDTO);

            //查询数据库内tb_third_party_user表中是否有该第三方账号
            OAuth2ThirdAccountDO oAuth2ThirdAccountDO1 = oAuth2ThirdAccountDB.findByPrimaryKey(oAuth2ThirdAccountDO);

            Long tbUserId;// 第三方账号对应的用户id
            UserDO userDO;// 第三方账号对应的用户信息
            // 添加或更新改用户已在数据库内的信息
            if (Objects.isNull(oAuth2ThirdAccountDO1)){//判断用户的第三方平台账号是否已在数据库
                //在tb_user表插入新用户信息
                userDO = OAuth2ThirdAccountConvertor.INSTANCE.DTO2UserDO(oAuth2ThirdAccountDTO);
                userDO.setPassword(UUID.randomUUID().toString());
                userDB.insert(userDO);

                //得到插入tb_user表后新用户信息的id(雪花算法生成ID)
                tbUserId = userDO.getId();

                //在tb_third_party_user表中新建第三方账号，外键user_id的值为tb_user表的id
                oAuth2ThirdAccountDO.setUserId(tbUserId);
                oAuth2ThirdAccountDB.insert(oAuth2ThirdAccountDO);
            }
            else{
                // 获取第三方账号对应的用户id
                tbUserId = oAuth2ThirdAccountDO1.getUserId();

                //更新第三方账号信息
                OAuth2ThirdAccountDO DO = OAuth2ThirdAccountConvertor.INSTANCE.DTO2DO(oAuth2ThirdAccountDTO);
                oAuth2ThirdAccountDB.update(DO);

                // 查询已有的用户信息
                userDO = userDB.selectById(tbUserId);
            }

            //将userDO转成LoginUser格式
            LoginUser loginUser = new LoginUser(userDO);

            //把完整信息存入redis，id作为key(如果原先有则覆盖)
            redisCache.setCacheObject("login:"+tbUserId, loginUser, Math.toIntExact(expiration / 1000 / 3600), TimeUnit.HOURS);
            String uniqueThirdId = registerationId + oAuth2ThirdAccountDTO.getProviderUserId();

            //把userId存入ThreadLocal, 方便Oauth2LoginSuccessHandler获取userId
            Oauth2UserIdContextHolder.setUserId(tbUserId);
        }

        // 返回用户信息
        return oAuth2User;
    }
}
