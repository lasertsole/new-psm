package com.psm.domain.User.user.adaptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDTO;
import com.psm.infrastructure.utils.MybatisPlus.Page.PageDTO;
import jakarta.validation.Valid;

public interface UserExtensionAdapter {
    /**
     * 插入用户扩展信息
     *
     * @param userExtensionDTO 用户扩展信息DTO
     */
    void insert(UserExtensionDTO userExtensionDTO);

    /**
     * 插入用户扩展信息
     *
     * @param userExtensionBO 用户扩展信息BO
     */
    void insert(UserExtensionBO userExtensionBO);

    /**
     * 根据id查询用户扩展信息
     *
     * @param userExtensionDTO 用户扩展信息DTO
     * @return 用户扩展信息BO
     */
    UserExtensionBO selectById(UserExtensionDTO userExtensionDTO);

    /**
     * 根据id查询用户扩展信息
     *
     * @param userExtensionBO 用户扩展信息BO
     * @return 用户扩展信息BO
     */
    UserExtensionBO selectById(@Valid UserExtensionBO userExtensionBO);

    /**
     * 更新用户扩展信息
     *
     * @param userExtensionDTO 用户扩展信息DTO
     * @return boolean
     */
    boolean updateById(UserExtensionDTO userExtensionDTO);

    /**
     * 更新用户扩展信息
     *
     * @param userExtensionBO 用户扩展信息BO
     * @return boolean
     */
    boolean updateById(UserExtensionBO userExtensionBO);

    /**
     * 根据id查询用户作品数量
     *
     * @param userExtensionDTO 用户扩展信息DTO实体
     * @return 用户作品数量DAO
     */
    UserExtensionDAO selectWorkNumById(UserExtensionDTO userExtensionDTO);

    /**
     * 根据id查询用户作品数量
     *
     * @param userExtensionBO 用户扩展信息BO实体
     * @return 用户作品数量DAO
     */
    UserExtensionDAO selectWorkNumById(UserExtensionBO userExtensionBO);


    /**
     * 更新用户作品数量
     *
     * @param userExtensionDTO 用户作品DTO实体
     * @return boolean
     */
    boolean updateModelNumById(UserExtensionDTO userExtensionDTO);

    /**
     * 更新用户作品数量
     *
     * @param userExtensionBO 用户作品BO实体
     * @return boolean
     */
    boolean updateModelNumById(UserExtensionBO userExtensionBO);

    /**
     * 更新用户作品数量
     *
     * @param id 用户id
     * @return boolean
     */
    boolean addOnePublicModelNumById(Long id);

    /**
     * 更新用户作品数量
     *
     * @param userExtensionDTO 用户作品DTO实体
     * @return boolean
     */
    boolean addOnePublicModelNumById(UserExtensionDTO userExtensionDTO);

    /**
     * 删除用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean removeOnePublicModelNumById(Long id);

    /**
     * 删除用户作品数量
     *
     * @param userExtensionDTO 用户作品DTO实体
     * @return boolean
     */
    boolean removeOnePublicModelNumById(UserExtensionDTO userExtensionDTO);

    /**
     * 更新用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return boolean
     */
    Long updateOneModelStorageById(Long id, Long storage);

    /**
     * 更新用户已用的存储空间
     *
     * @param userExtensionDTO 用户作品DTO实体
     * @return boolean
     */
    Long updateOneModelStorageById(UserExtensionDTO userExtensionDTO);

    /**
     * 增加用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long addOneModelStorageById(Long id, Long storage);

    /**
     * 增加用户已用的存储空间
     *
     * @param userExtensionDTO 用户作品DTO实体
     * @return 已用的存储空间
     */
    Long addOneModelStorageById(UserExtensionDTO userExtensionDTO);

    /**
     * 减少用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long minusOneModelStorageById(Long id, Long storage);

    /**
     * 减少用户已用的存储空间
     *
     * @param userExtensionDTO 用户作品DTO实体
     * @return 已用的存储空间
     */
    Long minusOneModelStorageById(UserExtensionDTO userExtensionDTO);

    /**
     * 根据id查询用户的作品数量
     *
     * @param pageDTO 分页参数
     * @return 用户扩展表信息列表
     */
    Page<UserExtensionBO> getHasPublicModelOrderByCreateTimeDesc(PageDTO pageDTO);
}
