package com.psm.domain.Independent.User.Single.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.User.Single.user.entity.LoginUser.LoginUser;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserRepository {
    /**
     * 数据库添加用户
     *
     * @param userDO 用户DO对象
     */
    void DBAddUser(UserDO userDO);

    /**
     * 数据库删除用户
     *
     * @param id 用户ID
     */
    void DBRemoveUser(Long id);

    /**
     * 数据库更新用户
     *
     * @param userDO 用户DO对象
     */
    void DBUpdateAvatar(UserDO userDO);

    /**
     * 数据库查询用户
     *
     * @param id 用户ID
     * @return 用户DO对象
     */
    UserDO DBSelectUser(Long id);

    /**
     * 数据库查询用户
     *
     * @param ids 用户ID列表
     * @return 用户DO对象列表
     */
    List<UserDO> DBSelectUsers(List<Long> ids);

    /**
     * 数据库查询用户
     *
     * @param page 页对象
     * @return 用户DO对象
     */
    List<UserDO> DBSelectUserOrderByCreateTimeAsc(Page<UserDO> page);

    /**
     * 数据库更新用户
     *
     * @param userDO 用户DO对象
     */
    Boolean DBUpdateInfo(UserDO userDO);

    /**
     * 数据库更新用户
     *
     * @param userDO 用户DO对象
     */
    String DBFindPasswordById(UserDO userDO);

    /**
     * 数据库更新用户
     *
     * @param userDO 用户DO对象
     */
    void DBUpdatePasswordById(UserDO userDO);

    /**
     * 数据库查询用户
     *
     * @param userDO 用户DO对象
     * @return 用户DO对象
     */
    List<UserDO> DBFindUsersByName(UserDO userDO);

    /**
     * 缓存登录用户
     *
     * @param id 用户ID
     * @param loginUser 登录用户对象
     */
    void cacheAddLoginUser(String id, LoginUser loginUser);

    /**
     * 移除缓存登录用户
     *
     * @param id 用户ID
     */
    void cacheRemoveLoginUser(String id);

    /**
     * 更新缓存用户
     *
     * @param userDO 用户DO对象
     */
    void cacheUpdateUser(UserDO userDO);

    /**
     * 获取缓存用户
     *
     * @param id 用户ID
     * @return 登录用户对象
     */
    LoginUser cacheSelectLoginUser(String id);

    /**
     * 更新OSS用户头像
     *
     * @param oldAvatar 用户旧头像
     * @param newAvatarFile 用户新头像文件
     * @param userId 用户ID
     * @return 新头像URL
     */
    String ossUpdateAvatar(String oldAvatar, MultipartFile newAvatarFile, String userId) throws Exception;
}
