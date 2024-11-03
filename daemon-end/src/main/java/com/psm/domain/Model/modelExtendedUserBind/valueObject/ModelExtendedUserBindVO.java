package com.psm.domain.Model.modelExtendedUserBind.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import com.psm.domain.User.user.entity.User.UserVO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelExtendedUserBindVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1810184549868629451L;

    ExtendedUserVO user;
    ModelVO model;
}
