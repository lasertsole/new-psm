package com.psm.domain.User.repository;

import com.psm.domain.User.entity.LoginUser.LoginUser;

import java.util.concurrent.TimeUnit;

public interface LoginUserRedis {
    /**
     * 添加登录用户
     *
     * @param id
     * @param loginUser
     */
    public void addLoginUser(String id, LoginUser loginUser);

    /**
     * 添加登录用户
     *
     * @param loginUser
     */
    public void addLoginUser(LoginUser loginUser);

    /**
     * 获取登录用户
     *
     * @param id
     * @return
     */
    public LoginUser getLoginUser(String id);

    /**
     * 获取登录用户
     *
     * @param loginUser
     * @return
     */
    public LoginUser getLoginUser(LoginUser loginUser);

    /**
     * 删除登录用户
     *
     * @param id
     */
    public void removeLoginUser(String id);

    /**
     * 删除登录用户
     *
     * @param loginUser
     */
    public void removeLoginUser(LoginUser loginUser);
}
