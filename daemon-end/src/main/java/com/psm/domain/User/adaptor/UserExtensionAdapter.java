package com.psm.domain.User.adaptor;

import com.psm.domain.User.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDTO;
import jakarta.validation.Valid;

public interface UserExtensionAdapter {
    /**
     * 插入用户扩展信息
     *
     * @param userExtensionDTO 用户扩展信息DTO
     */
    void insert(UserExtensionDTO userExtensionDTO);

    /**
     * 根据id查询用户扩展信息
     *
     * @param userExtensionDTO 用户扩展信息DTO
     * @return 用户扩展信息BO
     */
    UserExtensionBO selectById(UserExtensionDTO userExtensionDTO);

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
     * @param userExtensionDTO 用户扩展信息DTO
     * @return 用户作品数量
     */
    short selectWorkNumById(UserExtensionDTO userExtensionDTO);

    /**
     * 更新用户作品数量
     *
     * @param userExtensionDTO 用户作品数量DTO
     * @return boolean
     */
    boolean updateWorkNumById(UserExtensionDTO userExtensionDTO);

    /**
     * 更新用户作品数量(线程安全)
     *
     * @param userExtensionDTO 用户作品数量DTO
     * @return boolean
     */
    boolean addOneWorkNumById(UserExtensionDTO userExtensionDTO);
}
