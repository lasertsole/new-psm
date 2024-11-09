package com.psm.domain.Model.model.repository;

import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.types.enums.VisibleEnum;

import java.util.List;

public interface Model3dDB extends MPJDeepService<Model3dDAO> {
    /**
     * 插入模型数据
     * @param modelDAO
     */
    void insert(Model3dDAO modelDAO);

    /**
     * 根据模型ID查询模型
     *
     * @param modelId 模型ID
     * @param visibleEnum 可见性等级枚举
     * @return 模型DAO
     */
    Model3dDAO selectById(Long modelId, VisibleEnum visibleEnum);

    /**
     * 根据模型ID查询模型
     *
     * @param userIds 用户ID列表
     * @param visibleEnum 可见性等级枚举
     * @return 模型DAO
     */
    List<Model3dDAO> selectByUserIds(List<Long> userIds, VisibleEnum visibleEnum);

    /**
     * 删除模型数据
     *
     * @param modelDAO 模型DAO
     */
    void delete(Model3dDAO modelDAO);
}
