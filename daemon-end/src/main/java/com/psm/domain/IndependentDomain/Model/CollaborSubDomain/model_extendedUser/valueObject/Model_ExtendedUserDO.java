package com.psm.domain.IndependentDomain.Model.CollaborSubDomain.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.IndependentDomain.Model.model.entity.Model3dDO;
import com.psm.domain.IndependentDomain.Model.CollaborSubDomain.model_extendedUser.types.convertor.Model_ExtendedUserConvertor;
import com.psm.domain.IndependentDomain.User.relationships.valueObject.ExtendedUserDO;
import com.psm.types.common.DO.DO;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.Value;

import java.io.Serializable;

@Value
@AutoDefine
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserDO implements Serializable, DO<Model_ExtendedUserBO,Model_ExtendedUserDTO> {
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
