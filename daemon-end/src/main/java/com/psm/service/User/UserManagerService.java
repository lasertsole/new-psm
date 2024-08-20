package com.psm.service.User;

import com.psm.domain.User.UserDAO;
import com.psm.domain.UtilsDom.ResponseDTO;

/**用户应用服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface UserManagerService {
    /**
     * 登录
     *
     * @param user
     * @return
     */
    ResponseDTO login(UserDAO user);

    /**
     * 退出登录
     *
     * @return
     */
    ResponseDTO logout();

    /**
     * 注册
     *
     * @return
     */
    ResponseDTO register(UserDAO user);

    /**
     * 销号
     *
     * @return
     */
    ResponseDTO deleteUser();

    /**
     * 更新
     *
     * @return
     */
    public ResponseDTO updateUser(UserDAO user);

    /**
     * 更新密码
     *
     * @param password
     * @param changePassword
     * @return
     */
    public ResponseDTO updatePassword(String password, String changePassword);

    /**
     * 通过用户ID获取用户信息
     *
     * @param id
     * @return
     */
    public ResponseDTO getUserByID(Long id);

    /**
     * 通过用户名获取用户信息
     *
     * @param name
     * @return
     */
    public ResponseDTO getUserByName(String name);
}
