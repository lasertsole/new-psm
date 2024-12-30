package com.psm.domain.Independent.Model.Joint.model_extendedUser.repository;

import com.psm.domain.Independent.Model.Joint.model_extendedUser.valueObject.Model_ExtendedUserDO;

public interface Model_ExtendedUserRepository {

    Model_ExtendedUserDO DBSelectModelByModelId(Long id, Long userSelfId);
}
