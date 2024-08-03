package com.psm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.DTO.ResponseDTO;
import com.psm.domain.User;

public interface UserService extends IService<User> {
    /**
     * 登录
     *
     * @param user
     * @return
     */
    ResponseDTO login(User user);

    /**
     * 退出登录
     *
     * @return
     */
    ResponseDTO logout();

    /**
     * 注册
     *
     * @return
     */
    ResponseDTO register(User user);
}
