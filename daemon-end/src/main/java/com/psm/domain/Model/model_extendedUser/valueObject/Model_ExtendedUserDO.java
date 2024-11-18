package com.psm.domain.Model.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dDO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDO;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.Value;

import java.io.Serializable;

@Value
@AutoDefine
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserDO implements Serializable {
    ExtendedUserDO user;
    Model3dDO model;

    public static Model_ExtendedUserDO from(ExtendedUserDO user, Model3dDO model) {
        return new Model_ExtendedUserDO(user, model);
    }
}
