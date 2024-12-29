package com.psm.domain.Independent.Model.Joint.models_user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Model.Joint.models_user.repository.Models_UserDB;
import com.psm.domain.Independent.Model.Joint.models_user.service.Models_UserService;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserBO;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Models_UserServiceImpl implements Models_UserService {
    @Autowired
    private Models_UserDB Models_UserDB;

    @Override
    public Page<Models_UserBO> getModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId) {
        Page<Models_UserDO> modelsUserDOPage = Models_UserDB.selectModelsShowBars(current, size, isIdle, canUrgent, style, type, userSelfId);
        return Models_UserBO.fromDOPage(modelsUserDOPage);
    }
}
