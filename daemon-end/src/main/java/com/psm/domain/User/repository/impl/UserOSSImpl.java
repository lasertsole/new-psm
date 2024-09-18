package com.psm.domain.User.repository.impl;

import com.psm.infrastructure.annotation.spring.Repository;
import com.psm.domain.User.repository.UserOSS;
import com.psm.infrastructure.utils.OSS.UploadOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class UserOSSImpl implements UserOSS {
    @Value("${aliyun.oss.path.users.avatarFolderPath}")
    String avatarFolderPath;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    @Override
    public Boolean removeAvatar(String avatarUrl) throws Exception {
        return uploadOSSUtil.deleteFileByFullUrl(avatarUrl, avatarFolderPath);
    }

    @Override
    public String addAvatar(MultipartFile newAvatarFile) throws Exception {
        return uploadOSSUtil.multipartUpload(newAvatarFile, avatarFolderPath);
    }

    @Override
    public String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile) throws Exception{
        removeAvatar(oldAvatarUrl);
        return addAvatar(newAvatarFile);
    }
}
