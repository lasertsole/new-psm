package com.psm.infrastructure.RepositoryImpl.User.user.impl;

import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.User.Single.user.entity.LoginUser.LoginUser;
import com.psm.infrastructure.RepositoryImpl.User.user.LoginUserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

@Slf4j
@Repository
public class LoginUserCacheImpl implements LoginUserCache {

    private final Cache loginCache;

    public LoginUserCacheImpl(CacheManager cacheManager) {
        this.loginCache = cacheManager.getCache("loginUserCache");
    }

    @Override
    public void addLoginUser(String id, LoginUser loginUser){
        loginCache.put(id, loginUser);
    }

    @Override
    public LoginUser getLoginUser(String id){
        return loginCache.get(id, LoginUser.class);
    }

    @Override
    public void removeLoginUser(String id){
        loginCache.evict(id);
    }

    @Override
    public void updateLoginUser(UserDO userDO) {
        String id = String.valueOf(userDO.getId());
        LoginUser loginUser = loginCache.get(id, LoginUser.class);

        UserDO userDORefer = loginUser.getUserDO();//获取loginUser内的UserDO引用
        if (Objects.nonNull(userDO.getAvatar())) userDORefer.setAvatar(userDO.getAvatar());
        if (Objects.nonNull(userDO.getName())) userDORefer.setName(userDO.getName());
        if (Objects.nonNull(userDO.getProfile())) userDORefer.setProfile(userDO.getProfile());
        if (Objects.nonNull(userDO.getPhone())) userDORefer.setPhone(userDO.getPhone());
        if (Objects.nonNull(userDO.getEmail())) userDORefer.setEmail(userDO.getEmail());
        if (Objects.nonNull(userDO.getSex())) userDORefer.setSex(userDO.getSex());
        if (Objects.nonNull(userDO.getPassword())) userDORefer.setPassword(userDO.getPassword());
        if (Objects.nonNull(userDO.getIsIdle())) userDORefer.setIsIdle(userDO.getIsIdle());
        if (Objects.nonNull(userDO.getCanUrgent())) userDORefer.setCanUrgent(userDO.getCanUrgent());
        if (Objects.nonNull(userDO.getPublicModelNum())) userDORefer.setPublicModelNum(userDO.getPublicModelNum());
        if (Objects.nonNull(userDO.getModelMaxStorage())) userDORefer.setModelMaxStorage(userDO.getModelMaxStorage());

        addLoginUser(id, loginUser);
    }
}
