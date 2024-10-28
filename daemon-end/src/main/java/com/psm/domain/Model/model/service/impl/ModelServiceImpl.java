package com.psm.domain.Model.model.service.impl;

import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.domain.Model.model.infrastructure.convertor.ModelConvertor;
import com.psm.domain.Model.model.repository.ModelRedis;
import com.psm.domain.Model.model.repository.ModelDB;
import com.psm.domain.Model.model.repository.ModelOSS;
import com.psm.domain.Model.model.service.ModelService;
import com.psm.infrastructure.enums.VisibleEnum;
import com.psm.infrastructure.utils.Tus.TusUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

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

    @Autowired
    private ModelDB modelDB;

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

    @Transactional
    @Override
    public Map<String, Long> uploadModelInfo(ModelDTO modelDTO) throws Exception {
        log.info("visible is {}", modelDTO.getVisible());

        String userId = String.valueOf(modelDTO.getUserId());

        // 判断文件是否已上传完成且没有过期fullPath
        String fullName = modelRedis.getUploadModel(userId);

        if(StringUtils.isBlank(fullName))
            throw new RuntimeException("文件未上传完成或已过期");

        // 获取文件大小,单位Byte
        Long fileSize = tusUtil.getFileSize(fullName);

        // 将本地文件上传到OSS
        Map<String, String> ossResultMap;
        try {
            ossResultMap = modelOSS.addAllModel(tusUtil.getAbsoluteFilePathName(fullName), modelDTO.getCover(), userId);
        }
        catch (Exception e){
            throw new RuntimeException("文件上传失败");
        }

        Long modelId; // 定义模型ID
        try {
            // 将ModelDTO转换为ModelDAO
            ModelDAO modelDAO = ModelConvertor.INSTANCE.DTO2DAO(modelDTO);
            modelDAO.setEntity(ossResultMap.get("entityUrl"));
            modelDAO.setCover(ossResultMap.get("coverUrl"));
            modelDAO.setStorage(fileSize);

            // 将ModelDAO存入数据库
            modelDB.insert(modelDAO);
            modelId = modelDAO.getId();
        }
        catch (Exception e){
            // 回滚OSS
            modelOSS.deleteAllModel(ossResultMap.get("entityUrl"), ossResultMap.get("coverUrl"), userId);
            // 删除redis缓存
            modelRedis.removeUploadModel(userId);
            log.error("数据库写入失败", e);
            throw new RuntimeException("数据库写入失败");
        }

        try {
            // 删除redis缓存
            modelRedis.removeUploadModel(userId);

            //删除本地文件
            String folderName = tusUtil.getFolderName(fullName);
            tusFileUploadService.deleteUpload(folderName);
        }
        catch (Exception e) {
            // 回滚OSS
            modelOSS.deleteAllModel(ossResultMap.get("entityUrl"), ossResultMap.get("coverUrl"), userId);
            throw new RuntimeException("删除云文件失败");
        }

        return Map.of("modelId", modelId, "modelStorage", fileSize);
    }

    @Override
    public void removeModelInfo(ModelDTO modelDTO) throws IOException{
        modelDB.delete(ModelConvertor.INSTANCE.DTO2DAO(modelDTO));
    }

    @Override
    public ModelDAO selectById(Long modelId, VisibleEnum visibleEnum) {
        return modelDB.selectById(modelId, visibleEnum);
    }
}
