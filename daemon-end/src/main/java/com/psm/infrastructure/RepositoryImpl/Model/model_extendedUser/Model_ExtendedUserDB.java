package com.psm.infrastructure.RepositoryImpl.Model.model_extendedUser;

import com.psm.domain.Independent.Model.Joint.model_extendedUser.pojo.valueObject.Model_ExtendedUserDO;

public interface Model_ExtendedUserDB {
    /**
     * 根据模型id查询模型扩展信息
     *
     * @param id 模型id
     * @param userSelfId 发起查看的用户自身id
     * @return 模型扩展信息DO
     */
    Model_ExtendedUserDO selectModelByModelId(Long id, Long userSelfId);
}
