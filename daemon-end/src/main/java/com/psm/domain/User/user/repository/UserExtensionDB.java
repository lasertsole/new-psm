package com.psm.domain.User.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;

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

    /**
     * 根据id查询用户的作品数量
     *
     * @param currentPage 当前页码
     * @param pageSize 一页显示多少条
     * @return 用户扩展表信息页
     */
    Page<UserExtensionDAO> getHasPublicModelOrderByCreateTimeDesc(Integer currentPage, Integer pageSize);
}
