package com.psm.domain.User.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;

public interface UserExtensionDB extends IService<UserExtensionDAO>{
    /**
     * 插入用户扩展信息
     *
     * @param userExtensionDAO 用户扩展表信息
     */
    void insert(UserExtensionDAO userExtensionDAO);

    /**
     * 根据id查询用户扩展信息
     *
     * @param userExtensionDAO 用户扩展表信息
     * @return 用户扩展表信息
     */
    UserExtensionDAO selectById(UserExtensionDAO userExtensionDAO);

    /**
     * 更新用户扩展信息
     *
     * @param userExtensionDAO 用户扩展表信息
     * @return boolean
     */
    boolean updateById(UserExtensionDAO userExtensionDAO);
}
