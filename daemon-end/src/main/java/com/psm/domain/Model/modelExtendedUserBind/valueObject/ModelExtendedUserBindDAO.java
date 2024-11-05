package com.psm.domain.Model.modelExtendedUserBind.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.User.follower.valueObject.ExtendedUserDAO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelExtendedUserBindDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6176006044087415413L;

    ExtendedUserDAO user;
    ModelDAO model;

    public static ModelExtendedUserBindDAO from(ExtendedUserDAO user, ModelDAO model) {
        return new ModelExtendedUserBindDAO(user, model);
    }
}
