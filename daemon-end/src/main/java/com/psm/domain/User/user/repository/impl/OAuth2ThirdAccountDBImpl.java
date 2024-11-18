package com.psm.domain.User.user.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.infrastructure.DB.OAuth2ThirdAccountMapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.User.user.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDO;
import com.psm.domain.User.user.repository.OAuth2ThirdAccountDB;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class OAuth2ThirdAccountDBImpl extends ServiceImpl<OAuth2ThirdAccountMapper, OAuth2ThirdAccountDO> implements OAuth2ThirdAccountDB {
    @Autowired
    private OAuth2ThirdAccountMapper oauth2Mapper;

    public void insert(OAuth2ThirdAccountDO oAuth2ThirdAccountDAO){
        oauth2Mapper.insert(oAuth2ThirdAccountDAO);
    }

    @Override
    public void update(OAuth2ThirdAccountDO oAuth2ThirdAccountDAO){
        LambdaUpdateWrapper<OAuth2ThirdAccountDO> oAuth2ThirdAccountUpdateWrapper = new LambdaUpdateWrapper<>();
        oAuth2ThirdAccountUpdateWrapper.eq(OAuth2ThirdAccountDO::getRegistrationId, oAuth2ThirdAccountDAO.getRegistrationId())
                .and(w -> w.eq(OAuth2ThirdAccountDO::getProviderUserId, oAuth2ThirdAccountDAO.getProviderUserId()))
                .set(!ObjectUtil.isEmpty(oAuth2ThirdAccountDAO.getCredentials()), OAuth2ThirdAccountDO::getCredentials, oAuth2ThirdAccountDAO.getCredentials())
                .set(!ObjectUtil.isEmpty(oAuth2ThirdAccountDAO.getCredentialsExpiresAt()), OAuth2ThirdAccountDO::getCredentialsExpiresAt, oAuth2ThirdAccountDAO.getCredentialsExpiresAt());

        oauth2Mapper.update(null, oAuth2ThirdAccountUpdateWrapper);
    }

    @Override
    public OAuth2ThirdAccountDO findByPrimaryKey(OAuth2ThirdAccountDO oAuth2ThirdAccountDAO){
        LambdaQueryWrapper<OAuth2ThirdAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OAuth2ThirdAccountDO::getRegistrationId, oAuth2ThirdAccountDAO.getRegistrationId())
                .and(w -> w.eq(OAuth2ThirdAccountDO::getProviderUserId, oAuth2ThirdAccountDAO.getProviderUserId()));
        return oauth2Mapper.selectOne(queryWrapper);
    }
}
