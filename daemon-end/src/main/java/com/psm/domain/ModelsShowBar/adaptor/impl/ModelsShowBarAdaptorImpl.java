package com.psm.domain.ModelsShowBar.adaptor.impl;

import com.psm.domain.ModelsShowBar.adaptor.ModelsShowBarAdaptor;
import com.psm.domain.ModelsShowBar.service.ModelsShowBarService;
import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarDAO;
import com.psm.infrastructure.annotation.spring.Adaptor;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.ObjectUtils;

import java.security.InvalidParameterException;
import java.util.List;

@Adaptor
public class ModelsShowBarAdaptorImpl implements ModelsShowBarAdaptor {
    @Autowired
    private ModelsShowBarService modelsShowBarService;

    @Override
    public List<ModelsShowBarDAO> selectModelsShowBarOrderByCreateTimeDesc(@Valid PageDTO pageDTO) {
        if (
                ObjectUtils.isEmpty(pageDTO.getCurrentPage())
                && ObjectUtils.isEmpty(pageDTO.getPageSize())
        )
            throw new InvalidParameterException("Invalid parameter");

        return modelsShowBarService.selectModelsShowBarOrderByCreateTimeDesc(pageDTO.getCurrentPage(), pageDTO.getPageSize());
    }
}
