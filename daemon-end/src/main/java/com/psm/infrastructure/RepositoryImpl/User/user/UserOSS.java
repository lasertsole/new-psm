package com.psm.infrastructure.RepositoryImpl.User.user;

import org.springframework.web.multipart.MultipartFile;

public interface UserOSS {
    /**
     * 删除用户头像
     *
     * @param avatarUrl 用户头像Url
     * @return 布尔值
     * @throws Exception 抛出异常
     */
    Boolean removeAvatar(String avatarUrl, String userId) throws Exception;

    /**
     * 上传用户头像
     *
     * @param newAvatarFile 新用户头像文件
     * @return 存在OSS的头像Url
     * @throws Exception 抛出异常
     */
    String addAvatar(MultipartFile newAvatarFile, String userId) throws Exception;

    /**
     * 更新用户头像
     *
     * @param oldAvatarUrl 旧头像Url
     * @param newAvatarFile 新头像文件
     * @return 新头像存在OSS的Url
     */
    String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile, String userId) throws Exception;

    /**
     * 根据用户名删除模型文件夹。在删除用户时会用到，删除用户时，会直接删除用户文件夹，不管模型文件夹是否为空
     *
     * @return 布尔值
     * @throws Exception 抛出异常
     */
    Boolean removeUserFolder(String userId) throws Exception;
}
