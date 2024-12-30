package com.psm.infrastructure.RepositoryImpl.User.user;

import com.psm.domain.Independent.User.Single.user.entity.LoginUser.LoginUser;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;

public interface LoginUserCache {
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
     * 更新登录用户
     *
     * @param userDO 用户实体
     */
    void updateLoginUser(UserDO userDO);
}
