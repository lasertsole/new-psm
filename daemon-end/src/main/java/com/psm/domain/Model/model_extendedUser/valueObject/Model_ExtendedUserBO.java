package com.psm.domain.Model.model_extendedUser.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model_extendedUser.types.convertor.Model_ExtendedUserConvertor;
import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.utils.VO.BO2VOable;
import lombok.Value;

import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model_ExtendedUserBO implements Serializable, BO2VOable<Model_ExtendedUserVO> {
    ExtendedUserBO user;
    Model3dBO model;

    public static Model_ExtendedUserBO from(ExtendedUserBO user, Model3dBO model) {
        return new Model_ExtendedUserBO(user, model);
    }
    @Override
    public Model_ExtendedUserVO toVO() { return Model_ExtendedUserConvertor.INSTANCE.BO2VO(this); }
}