package com.psm.domain.Model.modelsUserBind.valueObject;

import com.psm.domain.Model.modelsUserBind.entity.BriefModelVO;
import com.psm.domain.User.user.entity.User.UserVO.OtherUserVO;
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
public class ModelsUserBindVO implements Serializable{ // VO实体不具有值对象性质，可以通过set方法赋值，方便快速构建
    @Serial
    private static final long serialVersionUID = -9167306602106926094L;

    private OtherUserVO user;
    private List<BriefModelVO> models;

    // 值对象禁止二次赋值
    public void setUser(OtherUserVO user) {
        if (Objects.isNull(this.user)) this.user = user;
    }

    // 值对象禁止二次赋值
    public void setModels(List<BriefModelVO> models) {
        if (Objects.isNull(this.models)) this.models = models;
    }
}
