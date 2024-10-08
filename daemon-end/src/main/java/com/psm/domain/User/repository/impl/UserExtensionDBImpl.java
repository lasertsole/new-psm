package com.psm.domain.User.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.repository.UserExtensionDB;
import com.psm.domain.User.repository.mapper.UserExtensionMapper;
import com.psm.infrastructure.annotation.spring.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class UserExtensionDBImpl extends ServiceImpl<UserExtensionMapper, UserExtensionDAO> implements UserExtensionDB {
    @Autowired
    private UserExtensionMapper userExtensionMapper;

    public void insert(UserExtensionDAO userExtension) {userExtensionMapper.insert(userExtension);}

    public UserExtensionDAO selectById(UserExtensionDAO userExtension) {return userExtensionMapper.selectById(userExtension.getId());}

    public boolean updateById(UserExtensionDAO userExtension) {
        userExtensionMapper.updateById(userExtension);
        return true;
    }
}
