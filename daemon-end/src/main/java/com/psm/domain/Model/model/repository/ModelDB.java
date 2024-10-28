package com.psm.domain.Model.model.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.infrastructure.enums.VisibleEnum;

public interface ModelDB extends IService<ModelDAO> {
    /**
     * 插入模型数据
     * @param modelDAO
     */
    void insert(ModelDAO modelDAO);

    /**
     * 根据模型ID查询模型
     *
     * @param modelId 模型ID
     * @param visibleEnum 可见性等级枚举
     * @return 模型DAO
     */
    ModelDAO selectById(Long modelId, VisibleEnum visibleEnum);

    /**
     * 删除模型数据
     *
     * @param modelDAO
     */
    void delete(ModelDAO modelDAO);
}
