package com.psm.domain.Model.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dVO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import lombok.Value;

import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserVO implements Serializable {
    ExtendedUserVO user;
    Model3dVO model;
}
