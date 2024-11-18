package com.psm.domain.Model.model_extendedUser.valueObject;

import com.psm.domain.Model.model_extendedUser.types.convertor.Model_ExtendedUserConvertor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDTO;
import com.psm.utils.VO.DTO2VOable;
import lombok.Value;

import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserDTO implements Serializable, DTO2VOable<Model_ExtendedUserVO> {
    ExtendedUserDTO user;
    Model3dDTO model;

    public static Model_ExtendedUserDTO fromDTO(ExtendedUserDTO user, Model3dDTO model) {
        return new Model_ExtendedUserDTO(user, model);
    }

    public static Model_ExtendedUserDTO fromBO(Model_ExtendedUserBO model_ExtendedUserBO) {
        return Model_ExtendedUserConvertor.INSTANCE.BO2DTO(model_ExtendedUserBO);
    }

    @Override
    public Model_ExtendedUserVO toVO() { return Model_ExtendedUserConvertor.INSTANCE.DTO2VO(this); }
}
