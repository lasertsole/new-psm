package com.psm.domain.Model.model_extendedUser.repository;

import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserDAO;

public interface Model_ExtendedUserDB {
    /**
     * 根据模型id查询模型扩展信息
     *
     * @param id 模型id
     * @param userSelfId 发起查看的用户自身id
     * @return 模型扩展信息DAO
     */
    Model_ExtendedUserDAO selectModelByModelId(Long id, Long userSelfId);
}
