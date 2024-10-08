package com.psm.domain.Model.service.impl;

import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.Model.infrastructure.ModelConvertor;
import com.psm.domain.Model.repository.ModelDB;
import com.psm.domain.Model.repository.ModelOSS;
import com.psm.domain.Model.repository.ModelRedis;
import com.psm.domain.Model.service.ModelService;
import com.psm.infrastructure.utils.Tus.TusUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void uploadModelInfo(ModelDTO modelDTO) throws TusException, IOException {
        String userId = String.valueOf(modelDTO.getUserId());

        // 判断文件是否已上传完成且没有过期fullPath
        String fullName = modelRedis.getUploadModel(userId);

        if(StringUtils.isBlank(fullName))
            throw new RuntimeException("文件未上传完成或已过期");

        // 将本地文件上传到OSS
        Map<String, String> ossResultMap;
        try {
            ossResultMap = modelOSS.addAllModel(tusUtil.getAbsoluteFilePathName(fullName), modelDTO.getCover(), userId);
        }
        catch (Exception e){
            throw new RuntimeException("文件上传失败");
        }

        try {
            // 将ModelDTO转换为ModelDAO
            ModelDAO modelDAO = ModelConvertor.INSTANCE.DTO2DAO(modelDTO);
            modelDAO.setEntity(ossResultMap.get("entityUrl"));
            modelDAO.setCover(ossResultMap.get("coverUrl"));

            // 将ModelDAO存入数据库
            modelDB.insert(modelDAO);
        }
        catch (Exception e){
            log.info("更新数据库失败{}", e);
        }

        try {
            // 删除redis缓存
            modelRedis.removeUploadModel(userId);

            //删除本地文件
            String folderName = tusUtil.getFolderName(fullName);
            tusFileUploadService.deleteUpload(folderName);
        }
        catch (Exception e) {
            log.error("删除文件失败{}", e);
        }

    }
}
