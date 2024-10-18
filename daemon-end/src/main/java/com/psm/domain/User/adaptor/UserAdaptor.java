package com.psm.domain.User.adaptor;

import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;
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
    UserBO getUserByID(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 通过用户名获取用户信息
     *
     * @param userDTO 用户DTO实体, 应包含 用户名
     * @return 用户BO实体列表
     */
    List<UserBO> getUserByName(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 按创建时间排序获取用户列表
     *
     * @param pageDTO 分页信息，应包含 当前页码 和 每页大小
     * @return 用户BO实体列表
     */
    List<UserBO> getUserOrderByCreateTimeAsc(PageDTO pageDTO);
}
