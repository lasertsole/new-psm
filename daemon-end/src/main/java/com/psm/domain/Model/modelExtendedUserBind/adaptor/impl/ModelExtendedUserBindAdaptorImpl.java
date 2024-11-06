package com.psm.domain.Model.modelExtendedUserBind.adaptor.impl;

import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Model.modelExtendedUserBind.adaptor.ModelExtendedUserBindAdaptor;
import com.psm.domain.Model.modelExtendedUserBind.service.ModelExtendedUserBindService;
import com.psm.domain.Model.modelExtendedUserBind.types.convertor.ModelExtendedUserBindConvertor;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindBO;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;

@Slf4j
@Adaptor
public class ModelExtendedUserBindAdaptorImpl implements ModelExtendedUserBindAdaptor {
    @Autowired
    private ModelExtendedUserBindService modelExtendedUserBindService;

    @Override
    public ModelExtendedUserBindBO getModelByModelId(Long id, Long userSelfId) {
        if (id == null || userSelfId == null) throw new InvalidParameterException("Invalid parameter");

        ModelExtendedUserBindDAO modelExtendedUserBindDAO = modelExtendedUserBindService.getModelByModelId(id, userSelfId);

        return ModelExtendedUserBindConvertor.INSTANCE.DAO2BO(modelExtendedUserBindDAO);
    }
}
