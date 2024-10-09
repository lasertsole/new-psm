package com.psm.domain.User.service;

import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**用户领域服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface UserService {
    /**
     * 获取当前登录用户信息
     *
     * @return 用户DAO实体
     */
    UserDAO getAuthorizedUser();

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
    Map<String, Object> login(UserDTO userDTO) throws LockedException, BadCredentialsException, DisabledException;


    /**
     * 退出登录
     */
    void logout();

    /**
     * 注册
     *
     * @param userDTO 用户DTO实体
     * @return 用户登录信息的键值对，其中token为令牌，user为用户信息
     */
    Map<String, Object> register(UserDTO userDTO) throws DuplicateKeyException;

    /**
     * 销号
     */
    void deleteUser();

    /**
     * 更新用户头像
     *
     * @param oldAvatarUrl 旧头像路径
     * @param newAvatarFile 新头像文件
     * @return String
     */
    String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile) throws Exception;

    /**
     * 更新用户信息(除了密码和头像)
     */
    void updateInfo(UserDTO userDTO);

    /**
     * 更新密码
     *
     * @param password 旧密码
     * @param changePassword 更新后的密码
     */
    void updatePassword(String password, String changePassword);

    /**
     * 通过用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户DAO实体
     */
    UserDAO getUserByID(Long id);

    /**
     * 通过用户名获取用户信息
     *
     * @param name 用户名
     * @return 根据用户名查找到的用户列表
     */
    List<UserDAO> getUserByName(String name);

    /**
     * 按创建时间排序获取用户列表
     *
     * @param current 当前页码
     * @param pageSize 每页用户信息条数
     * @return 用户信息列表
     */
    List<UserDAO> getUserOrderByCreateTimeAsc(Integer current, Integer pageSize);
}

