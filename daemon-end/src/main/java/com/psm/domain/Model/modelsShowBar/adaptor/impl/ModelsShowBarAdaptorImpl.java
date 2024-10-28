package com.psm.domain.Model.modelsShowBar.adaptor.impl;

import com.psm.domain.Model.modelsShowBar.adaptor.ModelsShowBarAdaptor;
import com.psm.domain.Model.modelsShowBar.infrastructure.convertor.ModelsShowBarConvertor;
import com.psm.domain.Model.modelsShowBar.service.ModelsShowBarService;
import com.psm.domain.Model.modelsShowBar.valueObject.ModelsShowBarBO;
import com.psm.domain.Model.modelsShowBar.valueObject.ModelsShowBarDAO;
import com.psm.infrastructure.annotation.spring.Adaptor;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.ObjectUtils;

import java.security.InvalidParameterException;
import java.util.List;

@Slf4j
@Adaptor
public class ModelsShowBarAdaptorImpl implements ModelsShowBarAdaptor {
    @Autowired
    private ModelsShowBarService modelsShowBarService;

    @Override
    public List<ModelsShowBarBO> selectModelsShowBarOrderByCreateTimeDesc(@Valid PageDTO pageDTO) {
        if (
                ObjectUtils.isEmpty(pageDTO.getCurrentPage())
                && ObjectUtils.isEmpty(pageDTO.getPageSize())
        )
            throw new InvalidParameterException("Invalid parameter");

        List<ModelsShowBarDAO> modelsShowBarDAOS = modelsShowBarService.getModelsShowBarOrderByCreateTimeDesc(pageDTO.getCurrentPage(), pageDTO.getPageSize());

        ModelsShowBarConvertor modelsShowBarConvertor = ModelsShowBarConvertor.INSTANCE;
        return modelsShowBarDAOS.stream().map(modelsShowBarConvertor::DAO2BO).toList();
    }
}
