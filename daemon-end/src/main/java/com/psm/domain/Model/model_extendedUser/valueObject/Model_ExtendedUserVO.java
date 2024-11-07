package com.psm.domain.Model.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -666570292050435619L;

    ExtendedUserVO user;
    ModelVO model;
}
