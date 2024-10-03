package com.psm.domain.Model.service.impl;

import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.Model.repository.ModelOSS;
import com.psm.domain.Model.repository.ModelRedis;
import com.psm.domain.Model.service.ModelService;
import com.psm.infrastructure.utils.OSS.UploadOSSUtil;
import com.psm.infrastructure.utils.Tus.TusUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private ModelOSS modelOSS;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        //判断是否是刚开始上传
        if (servletRequest.getMethod().equals("POST")){
            String fullName = modelRedis.getUploadModel(userId);

            //如果redis有值则删除原先的文件，防止重复上传
            if (fullName != null){
                String folderName = tusUtil.getFolderName(fullName);
                tusFileUploadService.deleteUpload(folderName);
            }
        }

        //处理请求
        tusFileUploadService.process(servletRequest, servletResponse);

        //判断是否上传完成, 如果是则将文件名存入redis
        if (tusUtil.isUploadCompleted(servletRequest)){
            String fullName = tusUtil.getFullName(servletRequest);
            modelRedis.addUploadModel(userId, fullName);
        };
    }

    @Override
    public void uploadModelInfo(ModelDTO modelDTO, String userId) throws TusException, IOException {
        // 判断文件是否已上传完成且没有过期fullPath
        String fullName = modelRedis.getUploadModel(userId);
        if(StringUtils.isBlank(fullName))
            throw new RuntimeException("文件未上传完成或已过期");

        // 将本地文件上传到OSS
        try {
            //TODO: 2024/10/3 上传文件到OSS
            modelOSS.addAllModel(tusUtil.getAbsoluteFilePathName(fullName),userId);
        }
        catch (Exception e){
            throw new RuntimeException("文件上传失败");
        }
//
//
//        // 删除redis缓存
//
//        //删除本地文件
//        tusFileUploadService.deleteUpload(modelDTO.getEntityUrl());

        return;
    }
}
