package com.psm.domain.Model.models_user.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4572524392984007742L;

    UserDAO user;
    List<ModelDAO> models;
}
