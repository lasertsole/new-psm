package com.psm.domain.ModelsShowBar.service;

import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarDAO;

import java.util.List;

public interface ModelsShowBarService {
    List<ModelsShowBarDAO> selectModelsShowBarOrderByCreateTimeDesc(Integer currentPage, Integer pageSize);
}
