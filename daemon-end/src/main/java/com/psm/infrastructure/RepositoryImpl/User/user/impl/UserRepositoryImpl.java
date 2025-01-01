package com.psm.infrastructure.RepositoryImpl.User.user.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.User.Single.user.pojo.entity.LoginUser.LoginUser;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserDO;
import com.psm.domain.Independent.User.Single.user.repository.UserRepository;
import com.psm.infrastructure.RepositoryImpl.User.user.LoginUserCache;
import com.psm.infrastructure.RepositoryImpl.User.user.UserDB;
import com.psm.infrastructure.RepositoryImpl.User.user.UserOSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private UserDB userDB;

    @Autowired
    private UserOSS userOSS;

    @Autowired
    private LoginUserCache loginUserCache;

    @Override
    public void DBAddUser(UserDO userDO) {
        userDB.save(userDO);
    }

    @Override
    public void DBRemoveUser(Long id) {
        userDB.removeById(id);
    }

    @Override
    public void DBUpdateAvatar(UserDO userDO) {
        userDB.updateAvatar(userDO);
    }

    @Override
    public UserDO DBSelectUser(Long id) {
        return userDB.getById(id);
    }

    @Override
    public List<UserDO> DBSelectUsers(List<Long> ids) {
        return userDB.selectUserByIds(ids);
    }

    @Override
    public List<UserDO> DBSelectUserOrderByCreateTimeAsc(Page<UserDO> page) {
        return userDB.selectUserOrderByCreateTimeAsc(page);
    }

    @Override
    public Boolean DBUpdateInfo(UserDO userDO) {
        return userDB.updateInfo(userDO);
    }

    @Override
    public String DBFindPasswordById(UserDO userDO) {
        return userDB.findPasswordById(userDO);
    };

    @Override
    public List<UserDO> DBFindUsersByName(UserDO userDO) {
        return userDB.findUsersByName(userDO);
    };

    @Override
    public void DBUpdatePasswordById(UserDO userDO) {
        userDB.updatePasswordById(userDO);
    };

    @Override
    public void cacheAddLoginUser(String id, LoginUser loginUser) {
        loginUserCache.addLoginUser(id, loginUser);
    }

    @Override
    public void cacheRemoveLoginUser(String id) {
        loginUserCache.removeLoginUser(id);
    }

    @Override
    public void cacheUpdateUser(UserDO userDO) {
        loginUserCache.updateLoginUser(userDO);
    }

    @Override
    public LoginUser cacheSelectLoginUser(String id) {
        return loginUserCache.getLoginUser(id);
    }

    @Override
    public String ossUpdateAvatar(String oldAvatar, MultipartFile newAvatarFile, String userId) throws Exception {
        return userOSS.updateAvatar(oldAvatar, newAvatarFile, userId);
    }
}
