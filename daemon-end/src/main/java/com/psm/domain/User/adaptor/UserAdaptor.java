package com.psm.domain.User.adaptor;

import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.entity.User.UserVO;
import com.psm.infrastructure.utils.DTO.PageDTO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public interface UserAdaptor {
    /**
     * 获取当前登录用户信息
     *
     * @return UserDAO
     */
    UserVO getAuthorizedUser();

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
    void updateUser(UserDTO userDTO) throws InvalidParameterException;

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
    UserVO getUserByID(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 通过用户名获取用户信息
     *
     * @param userDTO
     * @return
     */
    List<UserVO> getUserByName(UserDTO userDTO) throws InvalidParameterException;

    /**
     * 按创建时间排序获取用户列表
     *
     * @param pageDTO
     * @return
     */
    List<UserVO> getUserOrderByCreateTimeAsc(PageDTO pageDTO);
}
