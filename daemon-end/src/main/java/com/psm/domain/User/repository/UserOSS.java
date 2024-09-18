package com.psm.domain.User.repository;

import org.springframework.web.multipart.MultipartFile;

public interface UserOSS {
    /**
     * 删除用户头像
     *
     * @param avatarUrl
     * @return
     * @throws Exception
     */
    Boolean removeAvatar(String avatarUrl) throws Exception;

    /**
     * 上传用户头像
     *
     * @param newAvatarFile
     * @return
     * @throws Exception
     */
    String addAvatar(MultipartFile newAvatarFile) throws Exception;

    /**
     * 更新用户头像
     *
     * @param oldAvatarUrl
     * @param newAvatarFile
     * @return
     */
    String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile) throws Exception;
}
