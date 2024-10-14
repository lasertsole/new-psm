package com.psm.domain.User.service;


import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDTO;
import lombok.Synchronized;

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
     * @return 用户作品数量
     */
    short selectWorkNumById(Long id);

    /**
     * 更新用户作品数量
     *
     * @param userId 用户ID
     * @param work_num 作品数量
     * @return boolean
     */
    boolean updateWorkNumById(Long userId, short work_num);

    /**
     * 更新用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean addOneWorkNumById(Long id);

    /**
     * 删除用户作品数量
     *
     * @param id 用户ID
     * @return boolean
     */
    boolean removeOneWorkNumById(Long id);
}
