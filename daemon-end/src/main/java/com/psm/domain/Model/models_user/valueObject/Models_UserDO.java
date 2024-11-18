package com.psm.domain.Model.models_user.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dDO;
import com.psm.domain.Model.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.User.user.entity.User.UserDO;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@AutoDefine
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserDO implements Serializable {
    UserDO user;
    List<Model3dDO> models;
}
