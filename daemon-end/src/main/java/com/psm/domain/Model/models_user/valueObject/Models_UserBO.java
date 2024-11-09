package com.psm.domain.Model.models_user.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.types.utils.VO.BO2VOable;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserBO implements Serializable, BO2VOable<Models_UserVO> {
    UserBO user;
    List<Model3dBO> models;

    @Override
    public Models_UserVO toVO() {
        return Models_UserConvertor.INSTANCE.BO2VO(this);
    }
}
