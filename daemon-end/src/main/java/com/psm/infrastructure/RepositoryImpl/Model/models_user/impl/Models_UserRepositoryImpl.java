package com.psm.infrastructure.RepositoryImpl.Model.models_user.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Model.Joint.models_user.repository.Models_UserRepository;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserDO;
import com.psm.infrastructure.RepositoryImpl.Model.models_user.Models_UserDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class Models_UserRepositoryImpl implements Models_UserRepository {
    @Autowired
    private Models_UserDB models_UserDB;

    @Override
    public Page<Models_UserDO> ESSelectModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId) {

        return models_UserDB.selectModelsShowBars(current, size, isIdle, canUrgent, style, type, userSelfId);
    }
}
