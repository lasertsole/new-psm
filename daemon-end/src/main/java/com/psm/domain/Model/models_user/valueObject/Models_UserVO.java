package com.psm.domain.Model.models_user.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dVO;
import com.psm.domain.User.user.entity.User.UserVO;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserVO implements Serializable{
    UserVO user;
    List<Model3dVO> models;
}
