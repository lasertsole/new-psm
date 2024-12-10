package com.psm.domain.User.user.repository.impl;

import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.repository.LoginUserRedis;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.User.user.entity.LoginUser.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

@Slf4j
@Repository
public class LoginUserRedisImpl implements LoginUserRedis {

    private final Cache loginCache;

    public LoginUserRedisImpl(CacheManager cacheManager) {
        this.loginCache = cacheManager.getCache("loginCache");
    }

    @Override
    public void addLoginUser(String id, LoginUser loginUser){
        loginCache.put("login:"+id, loginUser);
    }

    @Override
    public LoginUser getLoginUser(String id){
        return loginCache.get("login:"+id, LoginUser.class);
    }

    @Override
    public void removeLoginUser(String id){
        loginCache.evict("login:"+id);
    }

    @Override
    public void removeLoginUser(LoginUser loginUser){
        String id = loginUser.getUserDO().getId().toString();
        removeLoginUser(id);
    }

    @Override
    public void updateLoginUser(UserDO userDO) {
        String id = String.valueOf(userDO.getId());
        LoginUser loginUser = loginCache.get("login:"+id, LoginUser.class);

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
