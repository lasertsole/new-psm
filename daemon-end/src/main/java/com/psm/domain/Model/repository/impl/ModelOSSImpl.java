package com.psm.domain.Model.repository.impl;

import com.psm.domain.Model.repository.ModelOSS;
import com.psm.infrastructure.utils.OSS.UploadOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class ModelOSSImpl implements ModelOSS {
    @Value("${aliyun.oss.path.models.coverFolderPath}")
    String coverFolderPath;

    @Value("${aliyun.oss.path.models.entityFolderPath}")
    String entityFolderPath;

    @Autowired
    private UploadOSSUtil uploadOSSUtil;

    private String proccessEntityFolderPath(String userId){
        return entityFolderPath.replace("{userId}", userId);
    }

    private String proccessCoverFolderPath(String userId){
        return coverFolderPath.replace("{userId}", userId);
    }

    @Override
    public String addModelEntity(String localFilePath, String userId) throws Exception {
        return uploadOSSUtil.multipartUpload(localFilePath, proccessEntityFolderPath(userId));
    }

    @Override
    public Boolean deleteModelEntity(String entityUrl, String userId) throws Exception {
        return uploadOSSUtil.deleteFileByFullUrl(entityUrl, proccessEntityFolderPath(userId));
    }

    @Override
    public String updateModelEntity(String oldEntityOssUrl, String newLocalFilePath, String userId) throws Exception {
        try {
            deleteModelEntity(oldEntityOssUrl, userId);
        }
        catch (Exception e){
            throw e;
        }

        return addModelEntity(newLocalFilePath, userId);
    }

    @Override
    public Boolean deleteModelCover(String coverUrl, String userId) throws Exception {
        return uploadOSSUtil.deleteFileByFullUrl(coverUrl, proccessCoverFolderPath(userId));
    }

    @Override
    public String addCover(MultipartFile newAvatarFile, String userId) throws Exception {
        return uploadOSSUtil.multipartUpload(newAvatarFile, proccessCoverFolderPath(userId));
    }

    @Override
    public String updateCover(String oldCoverUrl, MultipartFile newAvatarFile, String userId) throws Exception{
        try {
            deleteModelCover(oldCoverUrl, userId);
        }
        catch (Exception e){
            throw e;
        }

        return addCover(newAvatarFile, userId);
    }

    @Override
    public Map<String, String> addAllModel(String localFilePath, MultipartFile coverFile, String userId) throws Exception {
        String entityUrl = addModelEntity(localFilePath, userId);
        String coverUrl = addCover(coverFile, userId);
        return Map.of("entityUrl", entityUrl, "coverUrl", coverUrl);
    }

    @Override
    public void deleteAllModel(String entityUrl, String coverUrl, String userId) throws Exception {
        deleteModelEntity(entityUrl, userId);
        deleteModelCover(coverUrl, userId);
    };

    @Override
    public Boolean removeModelFolder(String userId) throws Exception {
        return uploadOSSUtil.deleteFolderByFullUrl(proccessEntityFolderPath(userId));
    }
}
