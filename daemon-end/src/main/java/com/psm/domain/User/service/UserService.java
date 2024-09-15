package com.psm.domain.User.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
public interface UserService extends IService<UserDAO> {
    /**
     * 获取当前登录用户信息
     *
     * @return UserDAO
     */
    UserDAO getAuthorizedUser();

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
    Map<String, Object> login(UserDTO userDTO) throws LockedException, BadCredentialsException, DisabledException;


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
     * @param oldAvatarUrl
     * @param newAvatarFile
     * @return String
     */
    String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile);

    /**
     * 更新用户信息(除了密码和头像)
     *
     * @return
     */
    void updateUser(UserDTO userDTO);

    /**
     * 更新密码
     *
     * @param password
     * @param changePassword
     * @return
     */
    void updatePassword(String password, String changePassword);

    /**
     * 通过用户ID获取用户信息
     *
     * @param id
     * @return
     */
    UserDAO getUserByID(Long id);

    /**
     * 通过用户名获取用户信息
     *
     * @param name
     * @return
     */
    List<UserDAO> getUserByName(String name);

    /**
     * 按创建时间排序获取用户列表
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<UserDAO> getUserOrderByCreateTimeAsc(Integer currentPage, Integer pageSize);
}

