package com.psm.domain.Independent.Model.Joint.model_extendedUser.adaptor.impl;

import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.adaptor.Model_ExtendedUserAdaptor;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.service.Model_ExtendedUserService;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.valueObject.Model_ExtendedUserBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;

@Slf4j
@Adaptor
public class Model_ExtendedUserAdaptorImpl implements Model_ExtendedUserAdaptor {
    @Autowired
    private Model_ExtendedUserService modelExtendedUserBindService;

    @Override
    public Model_ExtendedUserBO getModelByModelId(Long id, Long userSelfId) {
        if (id == null || userSelfId == null) throw new InvalidParameterException("Invalid parameter");

        return modelExtendedUserBindService.getModelByModelId(id, userSelfId);
    }
}
