package com.psm.domain.User.user.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.User.user.repository.mapper.OAuth2ThirdAccountMapper;
import com.psm.infrastructure.annotation.spring.Repository;
import com.psm.domain.User.user.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;
import com.psm.domain.User.user.repository.OAuth2ThirdAccountDB;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class OAuth2ThirdAccountDBImpl extends ServiceImpl<OAuth2ThirdAccountMapper, OAuth2ThirdAccountDAO> implements OAuth2ThirdAccountDB {
    @Autowired
    private OAuth2ThirdAccountMapper oauth2Mapper;

    public void insert(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO){
        oauth2Mapper.insert(oAuth2ThirdAccountDAO);
    }

    @Override
    public void update(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO){
        LambdaUpdateWrapper<OAuth2ThirdAccountDAO> oAuth2ThirdAccountUpdateWrapper = new LambdaUpdateWrapper<>();
        oAuth2ThirdAccountUpdateWrapper.eq(OAuth2ThirdAccountDAO::getRegistrationId, oAuth2ThirdAccountDAO.getRegistrationId())
                .and(w -> w.eq(OAuth2ThirdAccountDAO::getProviderUserId, oAuth2ThirdAccountDAO.getProviderUserId()))
                .set(!ObjectUtil.isEmpty(oAuth2ThirdAccountDAO.getCredentials()), OAuth2ThirdAccountDAO::getCredentials, oAuth2ThirdAccountDAO.getCredentials())
                .set(!ObjectUtil.isEmpty(oAuth2ThirdAccountDAO.getCredentialsExpiresAt()), OAuth2ThirdAccountDAO::getCredentialsExpiresAt, oAuth2ThirdAccountDAO.getCredentialsExpiresAt());

        oauth2Mapper.update(null, oAuth2ThirdAccountUpdateWrapper);
    }

    @Override
    public OAuth2ThirdAccountDAO findByPrimaryKey(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO){
        LambdaQueryWrapper<OAuth2ThirdAccountDAO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OAuth2ThirdAccountDAO::getRegistrationId, oAuth2ThirdAccountDAO.getRegistrationId())
                .and(w -> w.eq(OAuth2ThirdAccountDAO::getProviderUserId, oAuth2ThirdAccountDAO.getProviderUserId()));
        return oauth2Mapper.selectOne(queryWrapper);
    }
}
