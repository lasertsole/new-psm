package com.psm.controller.User;

import com.psm.domain.User.UserDTO;
import com.psm.domain.UtilsDom.ResponseDTO;

public interface UserController {
    /**
     * 登录
     *
     * @param userDto
     * @return
     */
    public ResponseDTO login(UserDTO userDto);

    /**
     * 注册
     *
     * @param userDto
     * @return
     */
    public ResponseDTO register(UserDTO userDto);

    /**
     * 登出
     *
     * @return
     */
    public ResponseDTO logout();

    /**
     * 销号
     *
     * @return
     */
    public ResponseDTO deleteUser();

    /**
     * 更新用户信息(除了密码)
     *
     * @param userDto
     * @return
     */
    public ResponseDTO updateUser(UserDTO userDto);

    /**
     * 更新密码
     *
     * @param userDto
     * @return
     */
    public ResponseDTO updatePassword(UserDTO userDto);

    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    public ResponseDTO getUserByID(Long id);

    /**
     * 根据用户名获取用户
     *
     * @param name
     * @return
     */
    public ResponseDTO getUserByName(String name);
}
