package com.psm.domain.Model.adaptor.impl;

import com.psm.domain.Model.adaptor.ModelAdaptor;
import com.psm.domain.Model.service.ModelService;
import com.psm.infrastructure.annotation.spring.Adaptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Adaptor
public class ModelAdaptorImpl implements ModelAdaptor {
    @Autowired
    ModelService modelService;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        modelService.uploadModelEntity(servletRequest, servletResponse, userId);
    }
}
