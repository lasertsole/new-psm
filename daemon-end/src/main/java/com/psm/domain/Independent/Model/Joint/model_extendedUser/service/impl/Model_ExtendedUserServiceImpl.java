package com.psm.domain.Independent.Model.Joint.model_extendedUser.service.impl;

import com.psm.domain.Independent.Model.Joint.model_extendedUser.repository.Model_ExtendedUserRepository;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.service.Model_ExtendedUserService;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.types.convertor.Model_ExtendedUserConvertor;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.pojo.valueObject.Model_ExtendedUserBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Model_ExtendedUserServiceImpl implements Model_ExtendedUserService {
    @Autowired
    private Model_ExtendedUserRepository model_ExtendedUserRepository;

    @Override
    public Model_ExtendedUserBO getModelByModelId(Long id, Long userSelfId) {
        return Model_ExtendedUserConvertor.INSTANCE.DO2BO(model_ExtendedUserRepository.DBSelectModelByModelId(id, userSelfId));
    }
}
