package com.psm.domain.Model.model_extendedUser.service.impl;

import com.psm.domain.Model.model_extendedUser.repository.Model_ExtendedUserDB;
import com.psm.domain.Model.model_extendedUser.service.Model_ExtendedUserService;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Model_ExtendedUserServiceImpl implements Model_ExtendedUserService {
    @Autowired
    private Model_ExtendedUserDB modelExtendedUserBindDB;

    @Override
    public Model_ExtendedUserDAO getModelByModelId(Long id, Long userSelfId) {
        return modelExtendedUserBindDB.selectModelByModelId(id, userSelfId);
    }
}
