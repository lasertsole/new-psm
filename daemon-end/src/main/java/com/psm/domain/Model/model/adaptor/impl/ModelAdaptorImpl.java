package com.psm.domain.Model.model.adaptor.impl;

import com.psm.domain.Model.model.adaptor.ModelAdaptor;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.domain.Model.model.types.convertor.ModelConvertor;
import com.psm.domain.Model.model.service.ModelService;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.types.enums.VisibleEnum;
import com.psm.types.utils.Valid.ValidUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Adaptor
public class ModelAdaptorImpl implements ModelAdaptor {
    @Autowired
    ValidUtil validUtil;

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
                || StringUtils.isBlank(modelDTO.getStyle())
                || StringUtils.isBlank(modelDTO.getType())
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
    public ModelBO selectById(@Valid ModelDTO modelDTO) throws InvalidParameterException{
        if (
                Objects.isNull(modelDTO.getId())
                || Objects.isNull(modelDTO.getVisible())
        )
            throw new InvalidParameterException("Invalid parameter");

        ModelDAO modelDAO = modelService.getById(modelDTO.getId(), VisibleEnum.fromInteger(modelDTO.getVisible()));

        return ModelConvertor.INSTANCE.DAO2BO(modelDAO);
    }

    @Override
    public ModelBO selectById(Long id, Integer visible) throws InvalidParameterException, InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("id", id, "visible", visible), ModelDTO.class);

        ModelDAO modelDAO = modelService.getById(id, VisibleEnum.fromInteger(visible));

        return ModelConvertor.INSTANCE.DAO2BO(modelDAO);
    }

    @Override
    public List<ModelBO> getByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        List<ModelDAO> modelDAOs = modelService.getByUserIds(userIds, visibleEnum);
        ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

        return modelDAOs.stream().map(modelConvertor::DAO2BO).toList();
    }
}
