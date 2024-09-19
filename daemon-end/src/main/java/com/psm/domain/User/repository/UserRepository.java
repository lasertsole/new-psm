package com.psm.domain.User.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.User.entity.User.UserDAO;

import java.util.List;

public interface UserRepository extends IService<UserDAO> {
    /**
     * 插入用户
     *
     * @param userDAO
     */
    void insert(UserDAO userDAO);

    /**
     * 根据id查找用户
     *
     * @param userDAO
     * @return
     */
    UserDAO selectById(UserDAO userDAO);

    /**
     * 更新用户头像
     *
     * @param userDAO
     */
    void updateAvatar(UserDAO userDAO);

    /**
     * 修改用户信息（除了密码和头像）
     *
     * @param userDAO
     */
    void updateInfo(UserDAO userDAO);

    /**
     * 根据id查找用户密码
     *
     * @param userDAO
     * @return
     */
    String findPasswordById(UserDAO userDAO);

    /**
     * 根据id修改密码
     *
     * @param userDAO
     */
    void updatePasswordById(UserDAO userDAO);

    /**
     * 根据用户名查找相似用户
     *
     * @param userDAO
     * @return
     */
    UserDAO findUserByName(UserDAO userDAO);

    /**
     * 根据用户名查找相似用户
     *
     * @param userDAO
     * @return
     */
    List<UserDAO> findUsersByName(UserDAO userDAO);

    /**
     * 分页获取用户列表
     *
     * @param page
     * @return
     */
    List<UserDAO> getUserOrderByCreateTimeAsc (Page<UserDAO> page);
}
