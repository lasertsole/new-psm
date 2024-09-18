package com.psm.domain.User.repository.impl;

import com.psm.infrastructure.annotation.spring.Repository;
import com.psm.domain.User.entity.LoginUser.LoginUser;
import com.psm.domain.User.repository.LoginUserRedis;
import com.psm.infrastructure.utils.Redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

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
        String id = loginUser.getUser().getId().toString();
        addLoginUser(id, loginUser);
    }

    @Override
    public LoginUser getLoginUser(String id){
        return redisCache.getCacheObject("login:"+id);
    }

    @Override
    public LoginUser getLoginUser(LoginUser loginUser){
        String id = loginUser.getUser().getId().toString();
        return getLoginUser(id);
    }

    @Override
    public void removeLoginUser(String id){
        redisCache.deleteObject("login:"+id);
    }

    @Override
    public void removeLoginUser(LoginUser loginUser){
        String id = loginUser.getUser().getId().toString();
        removeLoginUser(id);
    }
}
