package com.psm.domain.User.user.service.impl;

import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDTO;
import com.psm.domain.User.user.infrastructure.convertor.UserExtensionConvertor;
import com.psm.domain.User.user.repository.UserExtensionDB;
import com.psm.domain.User.user.service.UserExtensionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public UserExtensionDAO selectWorkNumById(Long id) {
        UserExtensionDAO userExtensionDAO = new UserExtensionDAO(id);

        return userExtensionDB.selectById(userExtensionDAO);
    }

    @Override
    public boolean updateModelNumById(Long id, short work_num) {
        UserExtensionDAO userExtensionDAO = new UserExtensionDAO(id);
        userExtensionDAO.setPublicModelNum(work_num);

        return userExtensionDB.updateById(userExtensionDAO);
    }

    @Override
    public boolean addOneModelNumById(Long id) {
        UserExtensionDAO userExtensionDAO = selectWorkNumById(id);
        short work_num = userExtensionDAO.getPublicModelNum();

        return updateModelNumById(id, (short) (work_num + 1));
    }

    @Override
    public boolean removeOneModelNumById(Long id) {
        UserExtensionDAO userExtensionDAO = selectWorkNumById(id);
        short work_num = userExtensionDAO.getPublicModelNum();

        if ( work_num == 0) return false;

        if ( work_num < 0) return updateModelNumById(id, (short) 0);

        return updateModelNumById(id, (short) (work_num - 1));
    }

    @Override
    public Long updateOneModelStorageById(Long id, Long storage) {
        UserExtensionDAO userExtensionDAO = new UserExtensionDAO(id);
        userExtensionDAO.setModelCurStorage(storage);
        if(!userExtensionDB.updateById(userExtensionDAO)) throw new RuntimeException("The user does not exist.");

        return storage;
    }

    @Override
    public Long addOneModelStorageById(Long id, Long storage) {
        UserExtensionDAO userExtensionDAO = selectWorkNumById(id);
        long modelCurStorage = userExtensionDAO.getModelCurStorage();
        long modelMaxStorage = userExtensionDAO.getModelMaxStorage();
        long newStorage = modelCurStorage + storage;
        if (newStorage > modelMaxStorage) throw new RuntimeException("The storage exceeds the maximum limit.");

        return updateOneModelStorageById(id, modelCurStorage + storage);
    }

    @Override
    public Long minusOneModelStorageById(Long id, Long storage) {
        UserExtensionDAO userExtensionDAO = selectWorkNumById(id);
        long modelCurStorage = userExtensionDAO.getModelCurStorage();
        long newStorage = modelCurStorage - storage;
        if (newStorage < 0) newStorage = 0;

        return updateOneModelStorageById(id, newStorage);
    }

    @Override
    public List<UserExtensionDAO> getHasPublicModelOrderByCreateTimeDesc(Integer currentPage, Integer pageSize) {
        return userExtensionDB.getHasPublicModelOrderByCreateTimeDesc(currentPage, pageSize);
    }


}
