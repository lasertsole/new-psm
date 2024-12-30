package com.psm.infrastructure.RepositoryImpl.Model.model3d;

public interface Model3dRedis {
    /**
     * 添加上传文件信息
     *
     * @param id 当前用户id
     * @param folderName 模型上传文件夹名
     */
    void addUploadModel(String id, String folderName);

    /**
     * 获取上传文件信息
     *
     * @param id 当前用户id
     * @return 模型所在文件夹名
     */
    String getUploadModel(String id);

    /**
     * 删除上传文件信息
     *
     * @param id 当前用户id
     */
    void removeUploadModel(String id);
}
