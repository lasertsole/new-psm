package com.psm.domain.IndependentDomain.User.user.adaptor;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.IndependentDomain.User.user.entity.User.UserBO;
import com.psm.utils.page.PageBO;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.security.InvalidParameterException;
import java.util.List;

public interface UserAdaptor {
    /**
     * 验证用户token
     *
     * @param token 用户token
     * @return 用户BO实体
     */
    UserBO authUserToken(String token);

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
     * @param userBO 用户BO实体
     * @return 用户BO实体
     */
    UserBO login(UserBO userBO) throws LockedException, BadCredentialsException, DisabledException, InvalidParameterException, InstantiationException, IllegalAccessException;

    /**
     * 登录socket
     *
     * @param srcClient 登录用户客户端
     */
    void socketLogin(SocketIOClient srcClient) throws ClientException, InstantiationException, IllegalAccessException;

    /**
     * 退出登录
     */
    void logout();

    /**
     * 注册
     *
     * @param userBO 用户BO实体
     * @return 用户BO实体，其中token为令牌，user为用户BO信息
     */
    UserBO register(UserBO userBO) throws DuplicateKeyException, InstantiationException, IllegalAccessException;

    /**
     * 销号
     */
    void deleteUser();

    /**
     * 更新用户头像
     *
     * @param userBO 用户BO实体，应包含 旧头像的路径 和 新头像的文件
     * @return String
     */
    String updateAvatar(UserBO userBO) throws InvalidParameterException, Exception;

    /**
     * 更新用户信息(除了密码和头像)
     *
     * @param userBO 用户BO实体，应包含 除了密码和头像 以外的信息
     */
    void updateInfo(UserBO userBO) throws InvalidParameterException, InstantiationException, IllegalAccessException;

    /**
     * 更新密码
     *
     * @param userBO 用户BO实体，应包含 新密码 和 旧密码
     */
    void updatePassword(UserBO userBO) throws InvalidParameterException;

    /**
     * 通过用户ID获取用户信息
     *
     * @param userBO 用户BO实体, 应包含 用户ID
     * @return 用户BO实体
     */
    UserBO getUserById(UserBO userBO) throws InvalidParameterException;

    /**
     * 通过用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户BO实体
     */
    UserBO getUserById(Long id) throws InvalidParameterException, InstantiationException, IllegalAccessException;

    /**
     * 通过用户名获取用户信息
     *
     * @param userBO 用户BO实体, 应包含 用户名
     * @return 用户BO实体列表
     */
    List<UserBO> getUserByName(UserBO userBO) throws InvalidParameterException, InstantiationException, IllegalAccessException;

    /**
     * 通过用户名获取用户信息
     *
     * @param name 用户名
     * @return 用户BO实体列表
     */
    List<UserBO> getUserByName(String name) throws InvalidParameterException, InstantiationException, IllegalAccessException;

    /**
     * 按创建时间排序获取用户列表
     *
     * @param pageBO 分页信息，应包含 当前页码 和 每页大小
     * @return 用户BO实体列表
     */
    List<UserBO> getUserOrderByCreateTimeAsc(PageBO pageBO);

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
     * @param userBO 用户BO实体
     * @return boolean
     */
    boolean updateOnePublicModelNumById(UserBO userBO);

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
     * @param userBO 用户BO实体
     * @return boolean
     */
    boolean addOnePublicModelNumById(UserBO userBO) throws InstantiationException, IllegalAccessException;

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
     * @param userBO 用户BO实体
     * @return boolean
     */
    boolean removeOnePublicModelNumById(UserBO userBO) throws InstantiationException, IllegalAccessException;

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
     * @param userBO 用户BO实体
     * @return boolean
     */
    Long updateOnePublicModelStorageById(UserBO userBO) throws InstantiationException, IllegalAccessException;

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
     * @param userBO 用户BO实体
     * @return 已用的存储空间
     */
    Long addOnePublicModelStorageById(UserBO userBO) throws InstantiationException, IllegalAccessException;

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
     * @param userBO 用户BO实体
     * @return 已用的存储空间
     */
    Long minusOnePublicModelStorageById(UserBO userBO) throws InstantiationException, IllegalAccessException;
}
