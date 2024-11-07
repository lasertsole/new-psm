package com.psm.domain.User.user.adaptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.types.utils.page.PageDTO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public interface UserAdaptor {
    /**
     * 获取当前登录用户信息
     *
     * @return 用户DAO实体
     */
    UserBO getAuthorizedUser();

    /**
     * 获取当前登录用户id
     *
     * @return 当前登录用户id
     */
    Long getAuthorizedUserId();

    /**
     * 登录
     *
     * @param userDTO 用户DTO实体
     * @return 用户登录信息的键值对，其中token为令牌，user为用户信息
     */
    Map<String, Object> login(UserDTO userDTO) throws LockedException, BadCredentialsException, DisabledException, InvalidParameterException;

    /**
     * 退出登录
     */
    void logout();

    /**
     * 注册
     *
     * @param userDTO 用户DTO实体
     * @return 用户登录信息的键值对，其中token为令牌，user为用户BO信息
     */
    Map<String, Object> register(UserDTO userDTO) throws DuplicateKeyException;

    /**
     * 销号
     */
    void deleteUser();

    /**
     * 更新用户头像
     *
     * @param userDTO 用户DTO实体，应包含 旧头像的路径 和 新头像的文件
     * @return String
     */
    String updateAvatar(UserDTO userDTO) throws InvalidParameterException, Exception;

    /**
     * 更新用户信息(除了密码和头像)
     *
     * @param userDTO 用户DTO实体，应包含 除了密码和头像 以外的信息
     */
    void updateInfo(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 更新密码
     *
     * @param userDTO 用户DTO实体，应包含 新密码 和 旧密码
     */
    void updatePassword(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 通过用户ID获取用户信息
     *
     * @param userDTO 用户DTO实体, 应包含 用户ID
     * @return 用户BO实体
     */
    UserBO getUserById(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 通过用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户BO实体
     */
    UserBO getUserById(Long id) throws InvalidParameterException;

    /**
     * 通过用户名获取用户信息
     *
     * @param userDTO 用户DTO实体, 应包含 用户名
     * @return 用户BO实体列表
     */
    List<UserBO> getUserByName(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 通过用户名获取用户信息
     *
     * @param name 用户名
     * @return 用户BO实体列表
     */
    List<UserBO> getUserByName(String name) throws InvalidParameterException;

    /**
     * 按创建时间排序获取用户列表
     *
     * @param pageDTO 分页信息，应包含 当前页码 和 每页大小
     * @return 用户BO实体列表
     */
    List<UserBO> getUserOrderByCreateTimeAsc(PageDTO pageDTO);

    /**
     * 根据id列表查找用户
     *
     * @param ids id列表
     * @return 用户DAO实体列表
     */
    List<UserBO> getUserByIds(List<Long> ids);

    /**
     * 更新用户作品数量
     *
     * @param userDTO 用户DTO实体
     * @return boolean
     */
    boolean updateOnePublicModelNumById(UserDTO userDTO);

    /**
     * 更新用户作品数量
     *
     * @param userBO 用户BO实体
     * @return boolean
     */
    boolean updateOnePublicModelNumById(UserBO userBO) throws InstantiationException, IllegalAccessException;

    /**
     * 更新用户作品数量
     *
     * @param id 用户id
     * @return boolean
     */
    boolean addOnePublicModelNumById(Long id) throws InstantiationException, IllegalAccessException;

    /**
     * 更新用户作品数量
     *
     * @param userDTO 用户DTO实体
     * @return boolean
     */
    boolean addOnePublicModelNumById(UserDTO userDTO) throws InstantiationException, IllegalAccessException;

    /**
     * 删除用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean removeOnePublicModelNumById(Long id) throws InstantiationException, IllegalAccessException;

    /**
     * 删除用户作品数量
     *
     * @param userDTO 用户DTO实体
     * @return boolean
     */
    boolean removeOnePublicModelNumById(UserDTO userDTO) throws InstantiationException, IllegalAccessException;

    /**
     * 更新用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return boolean
     */
    Long updateOnePublicModelStorageById(Long id, Long storage) throws InstantiationException, IllegalAccessException;

    /**
     * 更新用户已用的存储空间
     *
     * @param userDTO 用户DTO实体
     * @return boolean
     */
    Long updateOnePublicModelStorageById(UserDTO userDTO) throws InstantiationException, IllegalAccessException;

    /**
     * 增加用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long addOnePublicModelStorageById(Long id, Long storage) throws InstantiationException, IllegalAccessException;

    /**
     * 增加用户已用的存储空间
     *
     * @param userDTO 用户DTO实体
     * @return 已用的存储空间
     */
    Long addOnePublicModelStorageById(UserDTO userDTO) throws InstantiationException, IllegalAccessException;

    /**
     * 减少用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long minusOnePublicModelStorageById(Long id, Long storage) throws InstantiationException, IllegalAccessException;

    /**
     * 减少用户已用的存储空间
     *
     * @param userDTO 用户DTO实体
     * @return 已用的存储空间
     */
    Long minusOnePublicModelStorageById(UserDTO userDTO) throws InstantiationException, IllegalAccessException;
}
