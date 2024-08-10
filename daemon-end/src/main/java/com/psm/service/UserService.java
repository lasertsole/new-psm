package com.psm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.domain.User.UserDAO;


/**用户服务
 *
 * @author su
 * @date 2021/05/01
 */
public interface UserService extends IService<UserDAO> {
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
}

