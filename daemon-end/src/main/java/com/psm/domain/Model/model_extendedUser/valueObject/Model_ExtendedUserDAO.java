package com.psm.domain.Model.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDAO;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.Value;

import java.io.Serializable;

@Value
@AutoDefine
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserDAO implements Serializable {
    ExtendedUserDAO user;
    Model3dDAO model;

    public static Model_ExtendedUserDAO from(ExtendedUserDAO user, Model3dDAO model) {
        return new Model_ExtendedUserDAO(user, model);
    }
}
