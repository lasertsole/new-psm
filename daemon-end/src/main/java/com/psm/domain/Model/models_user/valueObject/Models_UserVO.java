package com.psm.domain.Model.models_user.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.User.user.entity.User.UserVO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserVO implements Serializable{
    @Serial
    private static final long serialVersionUID = -7694936338557299214L;

    UserVO user;
    List<ModelVO> models;
}
