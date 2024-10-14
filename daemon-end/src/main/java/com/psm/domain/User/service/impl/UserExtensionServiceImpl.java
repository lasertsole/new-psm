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

    public boolean updateWorkNumById(Long id, short work_num) {
        UserExtensionDAO userExtensionDAO = new UserExtensionDAO(id);
        userExtensionDAO.setModel_num(work_num);
        return userExtensionDB.updateById(userExtensionDAO);
    }

    public boolean addOneWorkNumById(Long id) {
        short work_num = selectWorkNumById(id);
        return updateWorkNumById(id, (short) (work_num + 1));
    }

    public boolean removeOneWorkNumById(Long id) {
        short work_num = selectWorkNumById(id);

        if ( work_num == 0) return false;

        if ( work_num < 0) return updateWorkNumById(id, (short) 0);

        return updateWorkNumById(id, (short) (work_num - 1));
    };
}
