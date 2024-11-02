package com.psm.domain.Model.modelsUserBind.valueObject;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.modelsUserBind.infrastructure.convertor.ModelsUserBindConvertor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Value
public class ModelsUserBindBO implements Serializable, BO2VOable<ModelsUserBindVO> {
    @Serial
    private static final long serialVersionUID = 2452678106298460928L;

    UserBO user;
    List<ModelBO> models;

    @Override
    public ModelsUserBindVO toVO() {
        return ModelsUserBindConvertor.INSTANCE.BO2VO(this);
    }
}
