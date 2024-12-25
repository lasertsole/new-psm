package com.psm.domain.IndependentDomain.Model.CollaborSubDomain.model_extendedUser.valueObject;

import com.psm.domain.IndependentDomain.Model.CollaborSubDomain.model_extendedUser.types.convertor.Model_ExtendedUserConvertor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.IndependentDomain.Model.model.entity.Model3dDTO;
import com.psm.domain.IndependentDomain.User.relationships.valueObject.ExtendedUserDTO;
import com.psm.types.common.DTO.DTO;
import lombok.Value;

import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserDTO implements Serializable, DTO<Model_ExtendedUserBO> {
    ExtendedUserDTO user;
    Model3dDTO model;

    public static Model_ExtendedUserDTO fromDTO(ExtendedUserDTO user, Model3dDTO model) {
        return new Model_ExtendedUserDTO(user, model);
    }

    public static Model_ExtendedUserDTO fromBO(Model_ExtendedUserBO model_ExtendedUserBO) {
        return Model_ExtendedUserConvertor.INSTANCE.BO2DTO(model_ExtendedUserBO);
    }

    @Override
    public Model_ExtendedUserBO toBO() {
        return Model_ExtendedUserConvertor.INSTANCE.DTO2BO(this);
    }
}
