package com.psm.domain.Independent.Model.Joint.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dDO;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.types.convertor.Model_ExtendedUserConvertor;
import com.psm.domain.Independent.User.Single.relationships.valueObject.ExtendedUserDO;
import com.psm.types.common.DO.DO;
import lombok.Value;


@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserDO implements DO<Model_ExtendedUserBO,Model_ExtendedUserDTO> {
    ExtendedUserDO user;
    Model3dDO model;

    public static Model_ExtendedUserDO from(ExtendedUserDO user, Model3dDO model) {
        return new Model_ExtendedUserDO(user, model);
    }

    @Override
    public Model_ExtendedUserBO toBO() {
        return Model_ExtendedUserConvertor.INSTANCE.DO2BO(this);
    }

    @Override
    public Model_ExtendedUserDTO toDTO() {
        return Model_ExtendedUserConvertor.INSTANCE.DO2DTO(this);
    }
}
