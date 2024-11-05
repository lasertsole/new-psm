package com.psm.domain.User.user.repository.impl;

import com.psm.domain.User.user.entity.User.UserDAO;
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
        String id = loginUser.getUserDAO().getId().toString();
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
        String id = loginUser.getUserDAO().getId().toString();
        removeLoginUser(id);
    }

    @Override
    public void updateLoginUser(UserDAO userDAO) {
        String id = String.valueOf(userDAO.getId());
        LoginUser loginUser = (LoginUser) redisCache.getCacheObject("login:" + id);

        UserDAO userDAORefer = loginUser.getUserDAO();//获取loginUser内的UserDAO引用
        if (!Objects.isNull(userDAO.getAvatar())) userDAORefer.setAvatar(userDAO.getAvatar());
        if (!Objects.isNull(userDAO.getName())) userDAORefer.setName(userDAO.getName());
        if (!Objects.isNull(userDAO.getProfile())) userDAORefer.setProfile(userDAO.getProfile());
        if (!Objects.isNull(userDAO.getPhone())) userDAORefer.setPhone(userDAO.getPhone());
        if (!Objects.isNull(userDAO.getEmail())) userDAORefer.setEmail(userDAO.getEmail());
        if (!Objects.isNull(userDAO.getSex())) userDAORefer.setSex(userDAO.getSex());
        if (!Objects.isNull(userDAO.getPassword())) userDAORefer.setPassword(userDAO.getPassword());

        addLoginUser(id, loginUser);
    }
}
