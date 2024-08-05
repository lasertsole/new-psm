package com.psm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.DTO.ResponseDTO;
import com.psm.domain.DAO.UserDAO;

public interface UserService extends IService<UserDAO> {
    /**
     * 登录
     *
     * @param user
     * @return
     */
    ResponseDTO login(UserDAO user);

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
    ResponseDTO register(UserDAO user);
}
