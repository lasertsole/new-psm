package com.psm.domain.Model.service.impl;

import com.psm.domain.Model.service.ModelService;
import com.psm.infrastructure.utils.Tus.TusProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    private TusFileUploadService tusFileUploadService;

    @Autowired
    private TusProperties tusProperties;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
        //处理请求
        tusFileUploadService.process(servletRequest, servletResponse);
    }

    public String uploadModelInfo(String EntityUrl) throws TusException, IOException {
        //删除本地文件
        tusFileUploadService.deleteUpload(EntityUrl);
        return null;
    }
}
