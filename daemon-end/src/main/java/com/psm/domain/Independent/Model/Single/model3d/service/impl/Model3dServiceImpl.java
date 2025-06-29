package com.psm.domain.Independent.Model.Single.model3d.service.impl;

import com.psm.domain.Independent.Model.Single.model3d.pojo.entity.Model3dBO;
import com.psm.domain.Independent.Model.Single.model3d.pojo.entity.Model3dDO;
import com.psm.domain.Independent.Model.Single.model3d.repository.Model3dRepository;
import com.psm.domain.Independent.Model.Single.model3d.service.Model3dService;
import com.psm.domain.Independent.Model.Single.model3d.types.convertor.Model3dConvertor;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;
import com.psm.types.common.Event.Event;
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
import org.springframework.web.multipart.MultipartFile;

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
    private MQPublisher mqPublisher;

    @Autowired
    private Model3dRepository modelRepository;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        //判断是否是刚开始上传
        if (servletRequest.getMethod().equals("POST")){
            String fullName = modelRepository.RedisSelectUploadModel(userId);

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
            modelRepository.RedisAddUploadModel(userId, fullName);
        };
    }

    @Override
    @Transactional
    public void uploadModelInfo(Long userId, String title, String content, MultipartFile coverFile, String style, String type, VisibleEnum visible) throws Exception {
        String _userId = String.valueOf(userId);

        // 判断文件是否已上传完成且没有过期fullPath
        String fullName = modelRepository.RedisSelectUploadModel(_userId);

        if(StringUtils.isBlank(fullName))
            throw new RuntimeException("文件未上传完成或已过期");

        // 获取文件大小,单位Byte
        Long fileSize = tus.getFileSize(fullName);

        // 将本地文件上传到OSS
        Map<String, String> ossResultMap;
        try {
            ossResultMap = modelRepository.OSSAddAllModel(tus.getAbsoluteFilePathName(fullName), coverFile, _userId);
        } catch (Exception e){
            throw new RuntimeException("文件上传失败", e);
        }

        // 将ModelDO存入数据库
        Long modelId; // 定义模型ID
        try {
            // 将ModelDTO转换为ModelDO
            Model3dDO model3dDO = new Model3dDO();
            model3dDO.setUserId(userId);
            model3dDO.setTitle(title);
            model3dDO.setContent(content);
            model3dDO.setStyle(style);
            model3dDO.setType(type);
            model3dDO.setVisible(visible);
            model3dDO.setEntity(ossResultMap.get("entityUrl"));
            model3dDO.setCover(ossResultMap.get("coverUrl"));
            model3dDO.setStorage(fileSize);

            // 将ModelDO存入数据库
            modelRepository.DBAddModel(model3dDO);
            modelId = model3dDO.getId();
        } catch (Exception e){
            // 回滚OSS
            modelRepository.OSSRemoveAllModel(ossResultMap.get("entityUrl"), ossResultMap.get("coverUrl"), _userId);
            // 删除redis缓存
            modelRepository.RedisRemoveUploadModel(_userId);
            log.error("数据库写入失败", e);
            throw new RuntimeException("数据库写入失败");
        }

        try {
            // 删除redis缓存
            modelRepository.RedisRemoveUploadModel(_userId);

            //删除本地文件
            String folderName = tus.getFolderName(fullName);
            tusFileUploadService.deleteUpload(folderName);
        } catch (Exception e) {
            // 回滚OSS
            modelRepository.OSSRemoveAllModel(ossResultMap.get("entityUrl"), ossResultMap.get("coverUrl"), _userId);
            throw new RuntimeException("删除云文件失败");
        }

        Model3dBO model3dBO = new Model3dBO();
        model3dBO.setId(modelId);
        model3dBO.setStorage(fileSize);
        model3dBO.setVisible(visible);
        Event<Model3dBO> uploadModel3DEvent = new Event<>(model3dBO, Model3dBO.class);

        // 将消息发送到MQ
        mqPublisher.publish(uploadModel3DEvent, "uploadModel3D", "USER");
    }

    @Override
    public void removeModelInfo(Long Id) throws IOException{
        modelRepository.DBRemoveModel(Id);
    }

    @Override
    public Model3dBO getById(Long modelId, VisibleEnum visibleEnum) {
        return Model3dConvertor.INSTANCE.DO2BO(modelRepository.DBSelectModel(modelId, visibleEnum));
    }

    @Override
    public List<Model3dBO> getByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        return modelRepository.DBSelectModels(userIds, visibleEnum).stream().map(Model3dConvertor.INSTANCE::DO2BO).toList();
    }

    @Override
    public List<Map<String, Object>> getBlurSearchModel3d(String keyword) throws IOException {
        return modelRepository.ESBlurSearchModel3d(keyword);
    }

    @Override
    public Map<String, Object> getDetailSearchModel3d(String keyword, Long afterKeyId, Integer size) throws IOException {
        return modelRepository.ESDetailSearchModel3d(keyword, afterKeyId, size);
    }
}
