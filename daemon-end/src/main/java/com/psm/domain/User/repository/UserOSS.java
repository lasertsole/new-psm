package com.psm.domain.User.repository;

import org.springframework.web.multipart.MultipartFile;

public interface UserOSS {
    /**
     * 删除用户头像
     *
     * @param avatarUrl 用户头像Url
     * @return 布尔值
     * @throws Exception
     */
    Boolean removeAvatar(String avatarUrl) throws Exception;

    /**
     * 上传用户头像
     *
     * @param newAvatarFile 新用户头像文件
     * @return 存在OSS的头像Url
     * @throws Exception
     */
    String addAvatar(MultipartFile newAvatarFile) throws Exception;

    /**
     * 更新用户头像
     *
     * @param oldAvatarUrl 旧头像Url
     * @param newAvatarFile 新头像文件
     * @return 新头像存在OSS的Url
     */
    String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile) throws Exception;
}
