package com.psm.domain.Model.modelsShowBar.service.impl;

import com.psm.domain.Model.modelsShowBar.service.ModelsShowBarService;
import com.psm.domain.Model.modelsShowBar.valueObject.ModelsShowBarDAO;
import com.psm.domain.Model.modelsShowBar.repository.ModelsShowBarDB;
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
    public List<ModelsShowBarDAO> getModelsShowBarOrderByCreateTimeDesc(Integer currentPage, Integer pageSize) {
        return modelsShowBarDB.selectModelsShowBarOrderByCreateTimeDesc(currentPage, pageSize);
    }
}
