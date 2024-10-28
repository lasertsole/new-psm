package com.psm.domain.Model.modelsShowBar.service;

import com.psm.domain.Model.modelsShowBar.valueObject.ModelsShowBarDAO;

import java.util.List;

public interface ModelsShowBarService {
    /**
     * 分页查询
     *
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return 模型盒子列表
     */
    List<ModelsShowBarDAO> getModelsShowBarOrderByCreateTimeDesc(Integer currentPage, Integer pageSize);
}
