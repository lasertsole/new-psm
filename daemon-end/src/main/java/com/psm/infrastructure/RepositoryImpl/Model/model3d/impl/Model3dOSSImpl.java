package com.psm.infrastructure.RepositoryImpl.Model.model3d.impl;

import com.psm.infrastructure.OSS.UploadOSS;
import com.psm.infrastructure.RepositoryImpl.Model.model3d.Model3dOSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class Model3dOSSImpl implements Model3dOSS {
    @Value("${aliyun.oss.path.models.coverFolderPath}")
    String coverFolderPath;

    @Value("${aliyun.oss.path.models.entityFolderPath}")
    String entityFolderPath;

    @Autowired
    private UploadOSS uploadOSS;

    private String proccessEntityFolderPath(String userId){
        return entityFolderPath.replace("{userId}", userId);
    }

    private String proccessCoverFolderPath(String userId){
        return coverFolderPath.replace("{userId}", userId);
    }

    @Override
    public String addModelEntity(String localFilePath, String userId) throws Exception {
        return uploadOSS.multipartUpload(localFilePath, proccessEntityFolderPath(userId));
    }

    @Override
    public Boolean deleteModelEntity(String entityUrl, String userId) throws Exception {
        return uploadOSS.deleteFileByFullUrl(entityUrl, proccessEntityFolderPath(userId));
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
        return uploadOSS.deleteFileByFullUrl(coverUrl, proccessCoverFolderPath(userId));
    }

    @Override
    public String addCover(MultipartFile newAvatarFile, String userId) throws Exception {
        return uploadOSS.multipartUpload(newAvatarFile, proccessCoverFolderPath(userId));
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
        return uploadOSS.deleteFolderByFullUrl(proccessEntityFolderPath(userId));
    }
}
