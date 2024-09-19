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
     * @return UserDAO
     */
    UserBO getAuthorizedUser();

    /**
     * 获取当前登录用户id
     *
     * @return Long
     */
    Long getAuthorizedUserId();

    /**
     * 登录
     *
     * @param userDTO
     * @return String token
     */
    Map<String, Object> login(UserDTO userDTO) throws LockedException, BadCredentialsException, DisabledException, InvalidParameterException;

    /**
     * 退出登录
     *
     * @return
     */
    void logout();

    /**
     * 注册
     *
     * @param userDTO
     * @return
     */
    Map<String, Object> register(UserDTO userDTO) throws DuplicateKeyException;

    /**
     * 销号
     *
     * @return
     */
    void deleteUser();

    /**
     * 更新用户头像
     *
     * @param userDTO
     * @return
     */
    String updateAvatar(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 更新用户信息(除了密码和头像)
     *
     * @param userDTO
     * @return
     */
    void updateInfo(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 更新密码
     *
     * @param userDTO
     * @return
     */
    void updatePassword(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 通过用户ID获取用户信息
     *
     * @param userDTO
     * @return
     */
    UserBO getUserByID(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 通过用户名获取用户信息
     *
     * @param userDTO
     * @return
     */
    List<UserBO> getUserByName(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 按创建时间排序获取用户列表
     *
     * @param pageDTO
     * @return
     */
    List<UserBO> getUserOrderByCreateTimeAsc(PageDTO pageDTO);
}
