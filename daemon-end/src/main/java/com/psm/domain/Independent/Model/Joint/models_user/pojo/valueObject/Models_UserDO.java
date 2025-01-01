package com.psm.domain.Independent.Model.Joint.models_user.pojo.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Model.Single.model3d.pojo.entity.Model3dDO;
import com.psm.domain.Independent.Model.Joint.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserDO;
import com.psm.types.common.POJO.DO;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserDO implements Serializable, DO<Models_UserBO, Models_UserDTO> {
    UserDO user;
    List<Model3dDO> models;

    @Override
    public Models_UserBO toBO() {
        return Models_UserConvertor.INSTANCE.DO2BO(this);
    }

    @Override
    public Models_UserDTO toDTO() {
        return Models_UserConvertor.INSTANCE.DO2DTO(this);
    }
}
