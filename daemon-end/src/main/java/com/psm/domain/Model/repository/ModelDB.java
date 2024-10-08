package com.psm.domain.Model.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.Model.entity.ModelDAO;

public interface ModelDB extends IService<ModelDAO> {
    /**
     * 插入模型数据
     * @param modelDAO
     */
    void insert(ModelDAO modelDAO);

    /**
     * 获取模型列表
     * @return 用户模型数据分页
     */
    Page<ModelDAO> getModelListByIds();
}
