package com.psm.domain.Model.service.impl;

import com.psm.domain.Model.repository.ModelRedis;
import com.psm.domain.Model.service.ModelService;
import com.psm.infrastructure.utils.Tus.TusUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    private TusFileUploadService tusFileUploadService;

    @Autowired
    TusUtil tusUtil;

    @Autowired
    private ModelRedis modelRedis;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        //判断是否是刚开始上传
        if (servletRequest.getMethod() == "POST"){
            String folderName = modelRedis.getUploadModel(userId);

            //如果redis有值则删除原先的文件，防止重复上传
            if (folderName != null){
                tusFileUploadService.deleteUpload(folderName);
            }
        }

        //处理请求
        tusFileUploadService.process(servletRequest, servletResponse);

        //判断是否上传完成, 如果是则将文件名存入redis
        if (tusUtil.isUploadCompleted(servletRequest)){
            String folderName = tusUtil.getFolderName(servletRequest);
            modelRedis.addUploadModel(userId, folderName);
        };
    }

    public String uploadModelInfo(String EntityUrl) throws TusException, IOException {
        //删除本地文件
        tusFileUploadService.deleteUpload(EntityUrl);
        return null;
    }
}
