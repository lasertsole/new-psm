package com.psm.domain.Independent.User.Single.user.repository;

import com.psm.domain.Independent.User.Single.user.entity.LoginUser.LoginUser;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;

public interface LoginUserRedis {
    /**
     * 添加登录用户
     *
     * @param id 登录用户id
     * @param loginUser 登录用户实体
     */
    void addLoginUser(String id, LoginUser loginUser);

    /**
     * 获取登录用户
     *
     * @param id 登录用户id
     * @return 登录用户实体
     */
    LoginUser getLoginUser(String id);

    /**
     * 删除登录用户
     *
     * @param id 登录用户id
     */
    void removeLoginUser(String id);

    /**
     * 删除登录用户
     *
     * @param loginUser 登录用户实体
     */
    void removeLoginUser(LoginUser loginUser);

    /**
     * 更新登录用户
     *
     * @param userDAO 用户实体
     */
    void updateLoginUser(UserDO userDAO);
}
