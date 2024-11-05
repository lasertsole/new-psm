package com.psm.domain.Model.modelExtendedUserBind.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.modelExtendedUserBind.types.convertor.ModelUserBindConvertor;
import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.types.utils.VO.BO2VOable;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelExtendedUserBindBO implements Serializable, BO2VOable<ModelExtendedUserBindVO> {
    @Serial
    private static final long serialVersionUID = -2497442996155030617L;

    ExtendedUserBO user;
    ModelBO model;

    public static ModelExtendedUserBindBO from(ExtendedUserBO user, ModelBO model) {
        return new ModelExtendedUserBindBO(user, model);
    }
    @Override
    public ModelExtendedUserBindVO toVO() { return ModelUserBindConvertor.INSTANCE.BO2VO(this); }
}