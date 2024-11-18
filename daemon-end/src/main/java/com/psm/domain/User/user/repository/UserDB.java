package com.psm.domain.User.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepository;

import java.util.List;

public interface UserDB extends BaseDBRepository<UserDO> {
    /**
     * 插入用户
     *
     * @param userDO 用户DO实体
     */
    void insert(UserDO userDO);

    /**
     * 根据id查找用户
     *
     * @param id 用户id
     * @return 查找到的用户DO实体
     */
    UserDO selectById(Long id);

    /**
     * 根据id查找用户
     *
     * @param userDO 用户DO实体,应包含要查找的id
     * @return 查找到的用户DO实体
     */
    UserDO selectById(UserDO userDO);

    /**
     * 更新用户头像
     *
     * @param userDO 用户DO实体，应包含要更新的id和avatarUrl
     */
    void updateAvatar(UserDO userDO);

    /**
     * 修改用户信息（除了密码和头像）
     *
     * @param userDO 用户DO实体，应包含要更新的id和要更新的信息
     */
    void updateInfo(UserDO userDO);

    /**
     * 根据id查找用户密码
     *
     * @param userDO 用户DO实体，应包含要查找的id
     * @return 查找到的密码
     */
    String findPasswordById(UserDO userDO);

    /**
     * 根据id修改密码
     *
     * @param userDO 用户DO实体，应包含要更新的id和要更新的密码
     */
    void updatePasswordById(UserDO userDO);

    /**
     * 根据用户名查找相似用户
     *
     * @param userDO 用户DO实体，应包含用户名
     * @return 查找到的用户DO实体
     */
    UserDO findUserByName(UserDO userDO);

    /**
     * 根据用户名查找相似用户
     *
     * @param userDO 用户DO实体，应包含用户名
     * @return 查找到的用户DO实体列表
     */
    List<UserDO> findUsersByName(UserDO userDO);

    /**
     * 分页获取用户列表
     *
     * @param page 分页信息
     * @return 按照创建时间升序排列的用户列表
     */
    List<UserDO> selectUserOrderByCreateTimeAsc (Page<UserDO> page);

    /**
     * 根据id列表查找用户
     *
     * @param ids id列表
     * @return 用户DO实体列表
     */
    List<UserDO> selectUserByIds(List<Long> ids);
}
