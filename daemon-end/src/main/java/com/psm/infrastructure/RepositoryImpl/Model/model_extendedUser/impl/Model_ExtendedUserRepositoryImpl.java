package com.psm.infrastructure.RepositoryImpl.Model.model_extendedUser.impl;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.repository.Model_ExtendedUserRepository;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.valueObject.Model_ExtendedUserDO;
import com.psm.infrastructure.RepositoryImpl.Model.model_extendedUser.Model_ExtendedUserDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class Model_ExtendedUserRepositoryImpl implements Model_ExtendedUserRepository {
    @Autowired
    private Model_ExtendedUserDB model_ExtendedUserDB;


    @Override
    public Model_ExtendedUserDO DBSelectModelByModelId(Long id, Long userSelfId) {
        return model_ExtendedUserDB.selectModelByModelId(id, userSelfId);
    }
}
