package com.psm.domain.User.user.repository.impl;

import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.repository.LoginUserRedis;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.User.user.entity.LoginUser.LoginUser;
import com.psm.infrastructure.Redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class LoginUserRedisImpl implements LoginUserRedis {
    @Value("${spring.security.jwt.expiration}")
    public Long expiration;//jwt有效期

    @Autowired
    private RedisCache redisCache;

    @Override
    public void addLoginUser(String id, LoginUser loginUser){
        redisCache.setCacheObject("login:"+id,loginUser,Math.toIntExact(expiration / 1000 / 3600), TimeUnit.HOURS);
    }

    @Override
    public void addLoginUser(LoginUser loginUser){
        String id = loginUser.getUserDO().getId().toString();
        addLoginUser(id, loginUser);
    }

    @Override
    public LoginUser getLoginUser(String id){
        return redisCache.getCacheObject("login:"+id);
    }

    @Override
    public void removeLoginUser(String id){
        redisCache.deleteObject("login:"+id);
    }

    @Override
    public void removeLoginUser(LoginUser loginUser){
        String id = loginUser.getUserDO().getId().toString();
        removeLoginUser(id);
    }

    @Override
    public void updateLoginUser(UserDO userDO) {
        String id = String.valueOf(userDO.getId());
        LoginUser loginUser = (LoginUser) redisCache.getCacheObject("login:" + id);

        UserDO userDORefer = loginUser.getUserDO();//获取loginUser内的UserDO引用
        if (!Objects.isNull(userDO.getAvatar())) userDORefer.setAvatar(userDO.getAvatar());
        if (!Objects.isNull(userDO.getName())) userDORefer.setName(userDO.getName());
        if (!Objects.isNull(userDO.getProfile())) userDORefer.setProfile(userDO.getProfile());
        if (!Objects.isNull(userDO.getPhone())) userDORefer.setPhone(userDO.getPhone());
        if (!Objects.isNull(userDO.getEmail())) userDORefer.setEmail(userDO.getEmail());
        if (!Objects.isNull(userDO.getSex())) userDORefer.setSex(userDO.getSex());
        if (!Objects.isNull(userDO.getPassword())) userDORefer.setPassword(userDO.getPassword());

        addLoginUser(id, loginUser);
    }
}
