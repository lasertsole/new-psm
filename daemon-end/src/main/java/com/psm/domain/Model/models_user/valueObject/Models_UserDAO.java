package com.psm.domain.Model.models_user.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@AutoDefine
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserDAO implements Serializable {
    UserDAO user;
    List<Model3dDAO> models;
}
