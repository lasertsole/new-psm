package com.psm.domain.Model.modelExtendedUserBind.service.impl;

import com.psm.domain.Model.modelExtendedUserBind.repository.ModelExtendedUserBindDB;
import com.psm.domain.Model.modelExtendedUserBind.service.ModelExtendedUserBindService;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModelExtendedUserBindServiceImpl implements ModelExtendedUserBindService {
    @Autowired
    private ModelExtendedUserBindDB modelExtendedUserBindDB;

    @Override
    public ModelExtendedUserBindDAO getModelByModelId(Long id, Long userSelfId) {
        return modelExtendedUserBindDB.selectModelByModelId(id, userSelfId);
    }
}
