package com.psm.infrastructure.RepositoryImpl.User.user;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.User.Single.user.repository.UserOSS;
import com.psm.infrastructure.OSS.UploadOSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class UserOSSImpl implements UserOSS {
    @Value("${aliyun.oss.path.users.avatarFolderPath}")
    String avatarFolderPath;

    @Autowired
    UploadOSS uploadOSS;

    private String proccessAvatarFolderPath(String userId){
        return avatarFolderPath.replace("{userId}", userId);
    };

    @Override
    public Boolean removeAvatar(String avatarUrl, String userId) throws Exception {
        return uploadOSS.deleteFileByFullUrl(avatarUrl, proccessAvatarFolderPath(userId));
    }

    @Override
    public String addAvatar(MultipartFile newAvatarFile, String userId) throws Exception {
        return uploadOSS.multipartUpload(newAvatarFile, proccessAvatarFolderPath(userId));
    }

    @Override
    public String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile, String userId) throws Exception{
        try {
            removeAvatar(oldAvatarUrl, userId);
        }
        catch (Exception e){
            throw e;
        }

        return addAvatar(newAvatarFile, userId);
    }

    @Override
    public Boolean removeUserFolder(String userId) throws Exception{
        return uploadOSS.deleteFolderByFullUrl(proccessAvatarFolderPath(userId));
    }
}
