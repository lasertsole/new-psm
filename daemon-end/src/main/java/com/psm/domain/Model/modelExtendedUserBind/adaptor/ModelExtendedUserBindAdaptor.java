package com.psm.domain.Model.modelExtendedUserBind.adaptor;

import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindBO;

public interface ModelExtendedUserBindAdaptor {
    /**
     * 根据模型id查询模型扩展信息
     *
     * @param id 模型id
     * @param userSelfId 发起查看的用户自身id
     * @return 模型扩展信息BO
     */
    ModelExtendedUserBindBO getModelByModelId(Long id, Long userSelfId);
}
