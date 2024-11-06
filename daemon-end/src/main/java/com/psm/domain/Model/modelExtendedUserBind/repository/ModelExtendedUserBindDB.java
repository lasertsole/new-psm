package com.psm.domain.Model.modelExtendedUserBind.repository;

import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindDAO;

public interface ModelExtendedUserBindDB {
    /**
     * 根据模型id查询模型扩展信息
     *
     * @param id 模型id
     * @param userSelfId 发起查看的用户自身id
     * @return 模型扩展信息DAO
     */
    ModelExtendedUserBindDAO selectModelByModelId(Long id, Long userSelfId);
}
