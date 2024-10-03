package com.psm.domain.Model.repository.impl;

import com.psm.domain.Model.repository.ModelOSS;
import com.psm.infrastructure.utils.OSS.UploadOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ModelOSSImpl implements ModelOSS {
    @Value("${aliyun.oss.path.models.coverFolderPath}")
    String coverFolderPath;

    @Value("${aliyun.oss.path.models.entityFolderPath}")
    String entityFolderPath;

    @Autowired
    private UploadOSSUtil uploadOSSUtil;

    @Override
    public Map<String, String> addAllModel(String localFilePath, String userId) throws Exception {
        uploadOSSUtil.multipartUpload(localFilePath, entityFolderPath + "/" + userId);
        return null;
    }
}
