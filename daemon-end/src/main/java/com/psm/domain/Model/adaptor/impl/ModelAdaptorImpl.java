package com.psm.domain.Model.adaptor.impl;

import com.psm.domain.Model.adaptor.ModelAdaptor;
import com.psm.domain.Model.entity.ModelBO;
import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.Model.infrastructure.convertor.ModelConvertor;
import com.psm.domain.Model.service.ModelService;
import com.psm.infrastructure.annotation.spring.Adaptor;
import com.psm.infrastructure.enums.VisibleEnum;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Objects;

@Adaptor
public class ModelAdaptorImpl implements ModelAdaptor {
    @Autowired
    ModelService modelService;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        modelService.uploadModelEntity(servletRequest, servletResponse, userId);
    }

    @Override
    public ModelBO uploadModelInfo(@Valid ModelDTO modelDTO) throws Exception {
        if (
                Objects.isNull(modelDTO.getUserId())
                || StringUtils.isBlank(modelDTO.getTitle())
                || StringUtils.isBlank(modelDTO.getContent())
                || Objects.isNull(modelDTO.getCover())
                || StringUtils.isBlank(modelDTO.getCategory())
                || Objects.isNull(modelDTO.getVisible())
        )
            throw new InvalidParameterException("Invalid parameter");

        Map<String, Long> map = modelService.uploadModelInfo(modelDTO);
        ModelBO modelBO = ModelConvertor.INSTANCE.DTO2BO(modelDTO);
        modelBO.setId(map.get("modelId"));
        modelBO.setStorage(map.get("modelStorage"));

        return modelBO;
    }

    @Override
    public ModelBO selectById(@Valid ModelDTO modelDTO) {
        if (
                Objects.isNull(modelDTO.getId())
                || Objects.isNull(modelDTO.getVisible())
        )
            throw new InvalidParameterException("Invalid parameter");

        ModelDAO modelDAO = modelService.selectById(modelDTO.getId(), VisibleEnum.fromInteger(modelDTO.getVisible()));

        return ModelConvertor.INSTANCE.DAO2BO(modelDAO);
    }

    ;
}
