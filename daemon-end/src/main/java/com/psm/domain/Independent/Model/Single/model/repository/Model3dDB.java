package com.psm.domain.Independent.Model.Single.model.repository;

import com.psm.domain.Independent.Model.Single.model.entity.Model3dDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepository;
import com.psm.types.enums.VisibleEnum;

import java.util.List;

public interface Model3dDB extends BaseDBRepository<Model3dDO> {
    /**
     * 插入模型数据
     * @param modelDO
     */
    void insert(Model3dDO modelDO);

    /**
     * 根据模型ID查询模型
     *
     * @param modelId 模型ID
     * @param visibleEnum 可见性等级枚举
     * @return 模型DO
     */
    Model3dDO selectById(Long modelId, VisibleEnum visibleEnum);

    /**
     * 根据模型ID查询模型
     *
     * @param userIds 用户ID列表
     * @param visibleEnum 可见性等级枚举
     * @return 模型DO
     */
    List<Model3dDO> selectByUserIds(List<Long> userIds, VisibleEnum visibleEnum);

    /**
     * 删除模型数据
     *
     * @param modelDO 模型DO
     */
    void delete(Model3dDO modelDO);
}
