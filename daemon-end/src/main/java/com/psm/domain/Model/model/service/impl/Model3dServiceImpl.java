package com.psm.domain.Model.model.service.impl;

import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.domain.Model.model.repository.Model3dRedis;
import com.psm.domain.Model.model.repository.Model3dDB;
import com.psm.domain.Model.model.repository.Model3dOSS;
import com.psm.domain.Model.model.service.Model3dService;
import com.psm.types.enums.VisibleEnum;
import com.psm.infrastructure.Tus.Tus;
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
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Model3dServiceImpl implements Model3dService {
    @Autowired
    private TusFileUploadService tusFileUploadService;

    @Autowired
    Tus tus;

    @Autowired
    private Model3dRedis modelRedis;

    @Autowired
    private Model3dOSS modelOSS;

    @Autowired
    private Model3dDB modelDB;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        //判断是否是刚开始上传
        if (servletRequest.getMethod().equals("POST")){
            String fullName = modelRedis.getUploadModel(userId);

            //如果redis有值则删除原先的文件，防止重复上传
            if (fullName != null){
                String folderName = tus.getFolderName(fullName);
                tusFileUploadService.deleteUpload(folderName);
            }
        }

        //处理请求
        tusFileUploadService.process(servletRequest, servletResponse);

        //判断是否上传完成, 如果是则将文件名存入redis
        if (tus.isUploadCompleted(servletRequest)){
            String fullName = tus.getFullName(servletRequest);
            modelRedis.addUploadModel(userId, fullName);
        };
    }

    @Transactional
    @Override
    public Map<String, Long> uploadModelInfo(Model3dDTO modelDTO) throws Exception {
        String userId = String.valueOf(modelDTO.getUserId());

        // 判断文件是否已上传完成且没有过期fullPath
        String fullName = modelRedis.getUploadModel(userId);

        if(StringUtils.isBlank(fullName))
            throw new RuntimeException("文件未上传完成或已过期");

        // 获取文件大小,单位Byte
        Long fileSize = tus.getFileSize(fullName);

        // 将本地文件上传到OSS
        Map<String, String> ossResultMap;
        try {
            ossResultMap = modelOSS.addAllModel(tus.getAbsoluteFilePathName(fullName), modelDTO.getCover(), userId);
        }
        catch (Exception e){
            throw new RuntimeException("文件上传失败");
        }

        // 将ModelDAO存入数据库
        Long modelId; // 定义模型ID
        try {
            // 将ModelDTO转换为ModelDAO
            Model3dDAO modelDAO = Model3dConvertor.INSTANCE.DTO2DAO(modelDTO);
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
            String folderName = tus.getFolderName(fullName);
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
    public void removeModelInfo(Model3dDTO modelDTO) throws IOException{
        modelDB.delete(Model3dConvertor.INSTANCE.DTO2DAO(modelDTO));
    }

    @Override
    public Model3dDAO getById(Long modelId, VisibleEnum visibleEnum) {
        return modelDB.selectById(modelId, visibleEnum);
    }

    @Override
    public List<Model3dDAO> getByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        return modelDB.selectByUserIds(userIds, visibleEnum);
    }
}
