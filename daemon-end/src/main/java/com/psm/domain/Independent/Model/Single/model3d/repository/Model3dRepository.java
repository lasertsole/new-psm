package com.psm.domain.Independent.Model.Single.model3d.repository;

import com.psm.domain.Independent.Model.Single.model3d.pojo.entity.Model3dDO;
import com.psm.types.enums.VisibleEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Model3dRepository {
    /**
     * 从redis获取用户上传的模型在本地完整路径名
     *
     * @param userId 用户id
     * @return 本地完整路径名
     */
    String RedisSelectUploadModel(String userId);

    /**
     * 添加用户上传的模型在本地完整路径名
     *
     * @param userId 用户id
     * @param fullName 本地完整路径名
     */
    void RedisAddUploadModel(String userId, String fullName);

    /**
     * 删除用户上传的模型在本地完整路径名
     *
     * @param userId 用户id
     */
    void RedisRemoveUploadModel(String userId);

    /**
     * 添加模型到OSS
     *
     * @param localFilePath 本地完整路径名
     * @param coverFile 封面文件
     * @param userId 用户id
     * @return 模型在OSS的完整路径名
     */
    Map<String, String> OSSAddAllModel(String localFilePath, MultipartFile coverFile, String userId) throws Exception;


    /**
     * 删除OSS模型文件
     *
     * @param entityUrl 模型实体文件在OSS中的路径
     * @param coverUrl 模型封面图片在OSS中的路径
     * @throws Exception 抛出异常
     */
    void OSSRemoveAllModel(String entityUrl, String coverUrl, String userId) throws Exception;

    /**
     * 添加模型到数据库
     *
     * @param modelDO 模型实体类
     */
    void DBAddModel(Model3dDO modelDO);

    /**
     * 删除模型
     *
     * @param Id 模型id
     */
    void DBRemoveModel(Long Id);

    /**
     * 通过id查询模型
     *
     * @param modelId 模型ID
     * @param visibleEnum 可见性等级枚举
     * @return 模型实体类
     */
    Model3dDO DBSelectModel(Long modelId, VisibleEnum visibleEnum);

    /**
     * 通过用户id查询模型
     *
     * @param userIds 用户id列表
     * @param visibleEnum 可见性等级
     * @return 模型实体类列表
     *
     */
    List<Model3dDO> DBSelectModels(List<Long> userIds, VisibleEnum visibleEnum);

    /**
     * 通过关键字模糊搜索模型
     *
     * @param keyword 关键字
     * @return 模型实体类列表
     * throws IOException 抛出IO异常
     */
    List<Map<String, Object>> ESBlurSearchModel3d(String keyword) throws IOException;

    /**
     * 通过关键字精确搜索模型
     *
     * @param keyword 关键字
     * @param afterKeyId 起始id
     * @param size 查询数量
     * @return 模型实体类列表
     * throws IOException 抛出IO异常
     */
    Map<String, Object> ESDetailSearchModel3d(String keyword, Long afterKeyId, Integer size) throws IOException;
}
