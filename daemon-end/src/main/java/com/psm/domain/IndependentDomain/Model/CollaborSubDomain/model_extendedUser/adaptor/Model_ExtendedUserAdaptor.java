package com.psm.domain.IndependentDomain.Model.CollaborSubDomain.model_extendedUser.adaptor;

import com.psm.domain.IndependentDomain.Model.CollaborSubDomain.model_extendedUser.valueObject.Model_ExtendedUserBO;

public interface Model_ExtendedUserAdaptor {
    /**
     * 根据模型id查询模型扩展信息
     *
     * @param id 模型id
     * @param userSelfId 发起查看的用户自身id
     * @return 模型扩展信息BO
     */
    Model_ExtendedUserBO getModelByModelId(Long id, Long userSelfId);
}
