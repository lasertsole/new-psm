package com.psm.domain.Model.modelUserBind.valueObject;

import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.User.user.entity.User.UserVO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
public class ModelUserBindVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8329376488522772766L;

    UserVO user;
    ModelVO model;
}
