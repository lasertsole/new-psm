package com.psm.domain.ModelsShowBar.valueObject;

import com.psm.domain.Model.entity.ModelBO;
import com.psm.domain.Model.entity.ModelVO;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelsShowBarVO implements Serializable{ // VO实体不具有值对象性质，可以通过set方法赋值，方便快速构建
    @Serial
    private static final long serialVersionUID = -7722104080157413205L;

    private OtherUserVO user;
    private List<ModelVO> models;

    // 值对象禁止二次赋值
    public void setUser(OtherUserVO user) {
        if (Objects.isNull(this.user)) this.user = user;
    }

    // 值对象禁止二次赋值
    public void setModels(List<ModelVO> models) {
        if (Objects.isNull(this.models)) this.models = models;
    }
}
