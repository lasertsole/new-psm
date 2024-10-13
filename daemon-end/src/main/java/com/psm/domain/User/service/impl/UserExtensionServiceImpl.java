package com.psm.domain.User.service.impl;

import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDTO;
import com.psm.domain.User.infrastructure.convertor.UserExtensionConvertor;
import com.psm.domain.User.repository.UserExtensionDB;
import com.psm.domain.User.service.UserExtensionService;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserExtensionServiceImpl implements UserExtensionService {
    @Autowired
    private UserExtensionDB userExtensionDB;


    @Override
    public void insert(Long id) {
        UserExtensionDAO userExtension = new UserExtensionDAO(id);
        userExtensionDB.insert(userExtension);
    }

    @Override
    public UserExtensionDAO selectById(Long id) {
        UserExtensionDAO userExtension = new UserExtensionDAO(id);
        return userExtensionDB.selectById(userExtension);
    }

    @Override
    public boolean updateById(UserExtensionDTO userExtensionDTO) {
        UserExtensionDAO userExtensionDAO = UserExtensionConvertor.INSTANCE.DTO2DAO(userExtensionDTO);
        return userExtensionDB.updateById(userExtensionDAO);
    }

    public short selectWorkNumById(Long id) {
        UserExtensionDAO userExtensionDAO = new UserExtensionDAO(id);
        return userExtensionDB.selectById(userExtensionDAO).getModel_num();
    }
}
