package com.psm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.ResponseResult;
import com.psm.domain.User;

public interface UserService extends IService<User> {
    /**
     * 登录
     *
     * @param user
     * @return
     */
    ResponseResult login(User user);

    /**
     * 退出登录
     *
     * @return
     */
    ResponseResult logout();

    /**
     * 注册
     *
     * @return
     */
    ResponseResult register(User user);
}
