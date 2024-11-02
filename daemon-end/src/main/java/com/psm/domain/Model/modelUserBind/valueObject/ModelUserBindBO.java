package com.psm.domain.Model.modelUserBind.valueObject;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.modelUserBind.infrastructure.convertor.ModelUserBindConvertor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
public class ModelUserBindBO implements Serializable, BO2VOable<ModelUserBindVO> { // BO实体具有值对象性质，只能通过构造方法赋值，不能通过set方法赋值
    @Serial
    private static final long serialVersionUID = -359785383629641704L;

    UserBO user;
    ModelBO model;

    @Override
    public ModelUserBindVO toVO() { return ModelUserBindConvertor.INSTANCE.BO2VO(this); }
}
