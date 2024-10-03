package com.psm.domain.Model.repository;

import java.util.Map;

public interface ModelOSS {
    /**
     * 上传模型文件
     *
     * @param localFilePath 模型实体文件在本地的路径
     * @param userId 用户id
     * @return 包含模型实体文件在OSS中的路径和封面图片在OSS中的路径的map
     * @throws Exception 抛出异常
     */
    Map<String, String> addAllModel(String localFilePath, String userId) throws Exception;
}
