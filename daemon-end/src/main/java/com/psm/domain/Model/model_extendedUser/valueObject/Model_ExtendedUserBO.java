package com.psm.domain.Model.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model_extendedUser.types.convertor.Model_ExtendedUserConvertor;
import com.psm.domain.User.relationships.valueObject.ExtendedUserBO;
import lombok.Value;

import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserBO implements Serializable {
    ExtendedUserBO user;
    Model3dBO model;

    public static Model_ExtendedUserBO fromBO(ExtendedUserBO user, Model3dBO model) {
        return new Model_ExtendedUserBO(user, model);
    }

    public static Model_ExtendedUserBO fromDO(Model_ExtendedUserDO model_ExtendedUserDO) {
        return Model_ExtendedUserConvertor.INSTANCE.DO2BO(model_ExtendedUserDO);
    }
}