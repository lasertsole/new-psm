package com.psm.domain.Independent.Model.Joint.models_user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Model.Joint.models_user.repository.Models_UserRepository;
import com.psm.infrastructure.RepositoryImpl.Model.models_user.Models_UserDB;
import com.psm.domain.Independent.Model.Joint.models_user.service.Models_UserService;
import com.psm.domain.Independent.Model.Joint.models_user.pojo.valueObject.Models_UserBO;
import com.psm.domain.Independent.Model.Joint.models_user.pojo.valueObject.Models_UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Models_UserServiceImpl implements Models_UserService {
    @Autowired
    private Models_UserDB models_UserDB;

    @Autowired
    private Models_UserRepository models_UserRepository;

    @Override
    public Page<Models_UserBO> getModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId) {
        Page<Models_UserDO> modelsUserDOPage = models_UserRepository.ESSelectModelsShowBars(current, size, isIdle, canUrgent, style, type, userSelfId);
        return Models_UserBO.fromDOPage(modelsUserDOPage);
    }
}
