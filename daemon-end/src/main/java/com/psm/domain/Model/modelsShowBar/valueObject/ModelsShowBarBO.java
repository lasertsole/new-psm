package com.psm.domain.Model.modelsShowBar.valueObject;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.modelsShowBar.infrastructure.convertor.ModelsShowBarConvertor;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ModelsShowBarBO implements Serializable, BO2VOable<ModelsShowBarVO> { // BO实体具有值对象性质，只能通过构造方法赋值，不能通过set方法赋值
    @Serial
    private static final long serialVersionUID = -8951884961490688395L;

    private final UserBO user;
    private final List<ModelBO> models;

    @Override
    public ModelsShowBarVO toVO() {
        return ModelsShowBarConvertor.INSTANCE.BO2VO(this);
    }
}
