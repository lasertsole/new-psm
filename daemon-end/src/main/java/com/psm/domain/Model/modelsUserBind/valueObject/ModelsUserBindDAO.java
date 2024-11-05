package com.psm.domain.Model.modelsUserBind.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelsUserBindDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3013519040901360571L;

    UserDAO user;
    List<ModelDAO> models;
}
