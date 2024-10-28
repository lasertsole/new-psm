package com.psm.domain.User.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.User.entity.User.UserDAO;

import java.util.List;

public interface UserDB extends IService<UserDAO> {
    /**
     * 插入用户
     *
     * @param userDAO 用户DAO实体
     */
    void insert(UserDAO userDAO);

    /**
     * 根据id查找用户
     *
     * @param userDAO 用户DAO实体,应包含要查找的id
     * @return 查找到的用户DAO实体
     */
    UserDAO selectById(UserDAO userDAO);

    /**
     * 更新用户头像
     *
     * @param userDAO 用户DAO实体，应包含要更新的id和avatarUrl
     */
    void updateAvatar(UserDAO userDAO);

    /**
     * 修改用户信息（除了密码和头像）
     *
     * @param userDAO 用户DAO实体，应包含要更新的id和要更新的信息
     */
    void updateInfo(UserDAO userDAO);

    /**
     * 根据id查找用户密码
     *
     * @param userDAO 用户DAO实体，应包含要查找的id
     * @return 查找到的密码
     */
    String findPasswordById(UserDAO userDAO);

    /**
     * 根据id修改密码
     *
     * @param userDAO 用户DAO实体，应包含要更新的id和要更新的密码
     */
    void updatePasswordById(UserDAO userDAO);

    /**
     * 根据用户名查找相似用户
     *
     * @param userDAO 用户DAO实体，应包含用户名
     * @return 查找到的用户DAO实体
     */
    UserDAO findUserByName(UserDAO userDAO);

    /**
     * 根据用户名查找相似用户
     *
     * @param userDAO 用户DAO实体，应包含用户名
     * @return 查找到的用户DAO实体列表
     */
    List<UserDAO> findUsersByName(UserDAO userDAO);

    /**
     * 分页获取用户列表
     *
     * @param page 分页信息
     * @return 按照创建时间升序排列的用户列表
     */
    List<UserDAO> selectUserOrderByCreateTimeAsc (Page<UserDAO> page);

    /**
     * 根据id列表查找用户
     *
     * @param ids id列表
     * @return 用户DAO实体列表
     */
    List<UserDAO> selectUserByIds(List<Long> ids);
}
