package com.psm.infrastructure.RepositoryImpl.Model.model3d.impl;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dDO;
import com.psm.domain.Independent.Model.Single.model3d.repository.Model3dRepository;
import com.psm.infrastructure.RepositoryImpl.Model.model3d.Model3dDB;
import com.psm.infrastructure.RepositoryImpl.Model.model3d.Model3dES;
import com.psm.infrastructure.RepositoryImpl.Model.model3d.Model3dOSS;
import com.psm.infrastructure.RepositoryImpl.Model.model3d.Model3dRedis;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class Model3dRepositoryImpl implements Model3dRepository {
    @Autowired
    private TusFileUploadService tusFileUploadService;

    @Autowired
    private Model3dRedis modelRedis;

    @Autowired
    private Model3dOSS modelOSS;

    @Autowired
    private Model3dDB modelDB;

    @Autowired
    private Model3dES modelES;

    @Override
    public String RedisSelectUploadModel(String userId) {
        return modelRedis.getUploadModel(userId);
    }

    @Override
    public void RedisAddUploadModel(String userId, String fullName) {
        modelRedis.addUploadModel(userId, fullName);
    }

    @Override
    public void RedisRemoveUploadModel(String userId) {
        modelRedis.removeUploadModel(userId);
    }

    @Override
    public Map<String, String> OSSAddAllModel(String localFilePath, MultipartFile coverFile, String userId) throws Exception {
        return modelOSS.addAllModel(localFilePath, coverFile, userId);
    }

    @Override
    public void OSSRemoveAllModel(String entityUrl, String coverUrl, String userId) throws Exception {
        modelOSS.deleteAllModel(entityUrl, coverUrl, userId);
    }

    @Override
    public void DBAddModel(Model3dDO modelDO) {
        modelDB.insert(modelDO);
    }

    @Override
    public void DBRemoveModel(Long Id) {
        modelDB.removeById(Id);
    }

    @Override
    public Model3dDO DBSelectModel(Long modelId, VisibleEnum visibleEnum) {
        return modelDB.selectById(modelId, visibleEnum);
    }

    @Override
    public List<Model3dDO> DBSelectModels(List<Long> userIds, VisibleEnum visibleEnum) {
        return modelDB.selectByUserIds(userIds, visibleEnum);
    }

    @Override
    public List<Map<String, Object>> ESBlurSearchModel3d(String keyword) throws IOException {
        return modelES.selectBlurSearchModel3d(keyword);
    }

    @Override
    public Map<String, Object> ESDetailSearchModel3d(String keyword, Long afterKeyId, Integer size) throws IOException {
        return modelES.selectDetailSearchModel3d(keyword, afterKeyId, size);
    }


}
