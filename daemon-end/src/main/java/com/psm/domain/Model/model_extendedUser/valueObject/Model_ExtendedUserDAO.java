package com.psm.domain.Model.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.User.follower.valueObject.ExtendedUserDAO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5183089664852457904L;

    ExtendedUserDAO user;
    ModelDAO model;

    public static Model_ExtendedUserDAO from(ExtendedUserDAO user, ModelDAO model) {
        return new Model_ExtendedUserDAO(user, model);
    }
}
