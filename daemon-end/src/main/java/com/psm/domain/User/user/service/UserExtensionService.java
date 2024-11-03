package com.psm.domain.User.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDTO;

public interface UserExtensionService {
    /**
     * 插入用户扩展信息
     *
     * @param userId 用户ID
     */
    void insert(Long userId);

    /**
     * 根据id查询用户扩展信息
     *
     * @param userId 用户ID
     * @return 用户扩展表信息
     */
    UserExtensionDAO selectById(Long userId);

    /**
     * 更新用户扩展信息
     *
     * @param userExtensionDTO 用户扩展信息DTO
     * @return boolean
     */
    boolean updateById(UserExtensionDTO userExtensionDTO);

    /**
     * 根据id查询用户作品数量
     *
     * @param id 用户ID
     * @return 用户扩展信息DAO
     */
    UserExtensionDAO selectWorkNumById(Long id);

    /**
     * 更新用户作品数量
     *
     * @param userId 用户ID
     * @param work_num 作品数量
     * @return boolean
     */
    boolean updateModelNumById(Long userId, short work_num);

    /**
     * 更新用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean addOneModelNumById(Long id);

    /**
     * 删除用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean removeOneModelNumById(Long id);

    /**
     * 更新用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return boolean
     */
    Long updateOneModelStorageById(Long id, Long storage);

    /**
     * 增加用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long addOneModelStorageById(Long id, Long storage);

    /**
     * 减少用户已用的存储空间
     *
     * @param id 用户ID
     * @param storage 存储空间
     * @return 已用的存储空间
     */
    Long minusOneModelStorageById(Long id, Long storage);

    /**
     * 根据id查询用户的作品数量
     *
     * @param currentPage 当前页码
     * @param pageSize 一页显示多少条
     * @return 用户扩展表信息页
     */
    Page<UserExtensionDAO> getHasPublicModelOrderByCreateTimeDesc(Integer currentPage, Integer pageSize);
}
