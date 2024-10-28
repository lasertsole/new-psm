package com.psm.domain.Model.modelsUserBind.valueObject;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.modelsUserBind.infrastructure.convertor.ModelsUserBindConvertor;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelsUserBindBO implements Serializable, BO2VOable<ModelsUserBindVO> { // BO实体具有值对象性质，只能通过构造方法赋值，不能通过set方法赋值
    @Serial
    private static final long serialVersionUID = 4673518805892072883L;

    private UserBO user;
    private List<ModelBO> models;

    @Override
    public ModelsUserBindVO toVO() {
        return ModelsUserBindConvertor.INSTANCE.BO2VO(this);
    }
}
