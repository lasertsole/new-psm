package com.psm.domain.ModelsShowBar.service.impl;

import com.psm.domain.ModelsShowBar.repository.ModelsShowBarDB;
import com.psm.domain.ModelsShowBar.service.ModelsShowBarService;
import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ModelsShowBarServiceImpl implements ModelsShowBarService {
    @Autowired
    private ModelsShowBarDB modelsShowBarDB;

    @Override
    public List<ModelsShowBarDAO> selectModelsShowBarOrderByCreateTimeDesc(Integer currentPage, Integer pageSize) {
        return modelsShowBarDB.selectModelsShowBarOrderByCreateTimeDesc(currentPage, pageSize);
    }
}
