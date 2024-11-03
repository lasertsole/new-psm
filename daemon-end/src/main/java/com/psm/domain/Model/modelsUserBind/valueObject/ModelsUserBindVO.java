package com.psm.domain.Model.modelsUserBind.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.User.user.entity.User.UserVO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelsUserBindVO implements Serializable{
    @Serial
    private static final long serialVersionUID = -1452683178506802830L;

    UserVO user;
    List<ModelVO> models;
}
