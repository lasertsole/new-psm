package com.psm.domain.Model.model.repository;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface Model3dOSS {

    /**
     * 上传模型实体文件
     *
     * @param localFilePath 模型实体文件在本地的路径
     * @param userId 用户id
     * @return 模型实体文件在OSS中的路径
     * @throws Exception 抛出异常
     */
    String addModelEntity(String localFilePath, String userId) throws Exception;

    /**
     * 删除模型实体文件
     *
     * @param entityUrl 模型实体文件在OSS中的路径
     * @param userId 用户id
     * @return 删除是否成功
     * @throws Exception 抛出异常
     */
    Boolean deleteModelEntity(String entityUrl, String userId) throws Exception;

    /**
     * 更新模型实体文件
     *
     * @param oldEntityOssUrl 模型实体文件在OSS中的路径
     * @param newLocalFilePath 新的模型实体文件在本地的路径
     * @param userId 用户id
     * @return 新的模型实体文件在OSS中的路径
     * @throws Exception 抛出异常
     */
    String updateModelEntity(String oldEntityOssUrl, String newLocalFilePath, String userId) throws Exception;

    /**
     * 删除模型封面图片
     *
     * @param coverUrl 模型封面图片在OSS中的路径
     * @param userId 用户id
     * @return 删除是否成功
     * @throws Exception 抛出异常
     */
    Boolean deleteModelCover(String coverUrl, String userId) throws Exception;

    /**
     * 上传模型封面图片
     *
     * @param newCoverFile 新的模型封面图片
     * @param userId 用户id
     * @return 封面图片在OSS中的路径
     * @throws Exception 抛出异常
     */
    String addCover(MultipartFile newCoverFile, String userId) throws Exception;


    /**
     * 更新模型封面图片
     *
     * @param oldCoverUrl 旧的封面图片在OSS中的路径
     * @param newCoverFile 新的封面图片
     * @param userId 用户id
     * @return 封面图片在OSS中的路径
     * @throws Exception 抛出异常
     */
    String updateCover(String oldCoverUrl, MultipartFile newCoverFile, String userId) throws Exception;

    /**
     * 上传模型文件
     *
     * @param localFilePath 模型实体文件在本地的路径
     * @param userId 用户id
     * @param coverFile 模型封面图片
     * @return 包含模型实体文件在OSS中的路径和封面图片在OSS中的路径的map
     * @throws Exception 抛出异常
     */
    Map<String, String> addAllModel(String localFilePath, MultipartFile coverFile, String userId) throws Exception;

    /**
     * 删除模型文件
     *
     * @param entityUrl 模型实体文件在OSS中的路径
     * @param coverUrl 模型封面图片在OSS中的路径
     * @return 删除是否成功
     * @throws Exception 抛出异常
     */
    void deleteAllModel(String entityUrl, String coverUrl, String userId) throws Exception;

    /**
     * 根据用户名删除模型文件夹。在删除用户时会用到，删除用户时，会直接删除用户文件夹，不管模型文件夹是否为空
     *
     * @param userId 用户id
     * @return 删除是否成功
     * @throws Exception 抛出异常
     */
    Boolean removeModelFolder(String userId) throws Exception;
}
