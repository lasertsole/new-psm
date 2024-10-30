package com.psm.domain.User.user.repository;

import com.psm.domain.User.user.entity.LoginUser.LoginUser;

public interface LoginUserRedis {
    /**
     * 添加登录用户
     *
     * @param id 登录用户id
     * @param loginUser 登录用户实体
     */
    public void addLoginUser(String id, LoginUser loginUser);

    /**
     * 添加登录用户
     *
     * @param loginUser 登录用户实体
     */
    public void addLoginUser(LoginUser loginUser);

    /**
     * 获取登录用户
     *
     * @param id 登录用户id
     * @return 登录用户实体
     */
    public LoginUser getLoginUser(String id);

    /**
     * 删除登录用户
     *
     * @param id 登录用户id
     */
    public void removeLoginUser(String id);

    /**
     * 删除登录用户
     *
     * @param loginUser 登录用户实体
     */
    public void removeLoginUser(LoginUser loginUser);
}
