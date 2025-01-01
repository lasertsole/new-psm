package com.psm.domain.Independent.User.Single.user.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserBO;
import com.psm.types.enums.VisibleEnum;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**用户领域服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface UserService {
    /**
     * 获取当前登录用户信息
     *
     * @return 用户BO实体
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
     * @param name 用户名
     * @param password 密码
     * @return 用户BO实体，其中token为令牌，user为用户信息
     */
    UserBO login(String name, String password) throws LockedException, BadCredentialsException, DisabledException;

    /**
     * 登录socket
     *
     * @param srcClient 登录用户客户端
     */
    void socketLogin(SocketIOClient srcClient) throws ClientException, InstantiationException, IllegalAccessException;

    /**
     * 广播登录socket
     *
     * @param userId 用户ID
     */
    void forwardSocketLogin(String userId);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 注册
     *
     * @param name 用户DTO实体
     * @param password 密码
     * @param email 邮箱
     * @return 用户BO实体，其中token为令牌，user为用户信息
     */
    UserBO register(String name, String password, String email) throws DuplicateKeyException;

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
    void updateInfo(String name, Boolean sex, String phone, String email, String profile, Boolean isIdle, Boolean canUrgent);

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
     * @return 用户BO实体
     */
    UserBO getUserByID(Long id);

    /**
     * 通过用户名获取用户信息
     *
     * @param name 用户名
     * @return 根据用户名查找到的用户BO列表
     */
    List<UserBO> getUserByName(String name);

    /**
     * 按创建时间排序获取用户列表
     *
     * @param current 当前页码
     * @param pageSize 每页用户信息条数
     * @return 用户BO实体列表
     */
    List<UserBO> getUserOrderByCreateTimeAsc(Integer current, Integer pageSize);

    /**
     * 根据id列表查找用户
     *
     * @param ids id列表
     * @return 用户BO实体列表
     */
    List<UserBO> getUserByIds(List<Long> ids);

    /**
     * 更新用户作品数量
     *
     * @param userId 用户ID
     * @param work_num 作品数量
     * @return boolean
     */
    boolean updateOnePublicModelNumById(Long userId, short work_num);

    /**
     * 更新用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean addOnePublicModelNumById(Long id);

    /**
     * 删除用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean removeOnePublicModelNumById(Long id);

    /**
     * 更新用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return boolean
     */
    Long updateOnePublicModelStorageById(Long id, Long storage);

    /**
     * 增加用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long addOnePublicModelStorageById(Long id, Long storage);

    /**
     * 减少用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long minusOnePublicModelStorageById(Long id, Long storage);

    /**
     * 上传模型后处理
     *
     * @param userId 用户ID
     * @param modelSize 模型大小
     * @param visible 可见性
     */
    void processUploadModel3D(Long userId, Long modelSize, VisibleEnum visible);
}

