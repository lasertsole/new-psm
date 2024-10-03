package com.psm.domain.Model.adaptor.impl;

import com.psm.domain.Model.adaptor.ModelAdaptor;
import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.Model.service.ModelService;
import com.psm.infrastructure.annotation.spring.Adaptor;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.InvalidParameterException;
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
    public void uploadModelInfo(ModelDTO modelDTO) throws TusException, IOException {
        if (
                Objects.isNull(modelDTO.getUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        modelService.uploadModelInfo(modelDTO);
    };
}
