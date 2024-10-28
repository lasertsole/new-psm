package com.psm.domain.Model.modelsShowBar.repository;

import com.psm.domain.Model.modelsShowBar.valueObject.ModelsShowBarDAO;

import java.util.List;

public interface ModelsShowBarDB {
    /**
     * 分页查询
     *
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return 模型展示栏DAO列表
     */
    List<ModelsShowBarDAO> selectModelsShowBarOrderByCreateTimeDesc(Integer currentPage, Integer pageSize);
}
