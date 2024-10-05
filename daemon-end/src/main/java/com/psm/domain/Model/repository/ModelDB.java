package com.psm.domain.Model.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.Model.entity.ModelDAO;

public interface ModelDB extends IService<ModelDAO> {
    /**
     * 插入模型数据
     * @param modelDAO
     */
    void insert(ModelDAO modelDAO);
}
