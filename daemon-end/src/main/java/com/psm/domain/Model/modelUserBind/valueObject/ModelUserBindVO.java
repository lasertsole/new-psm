package com.psm.domain.Model.modelUserBind.valueObject;

import com.psm.domain.Model.modelUserBind.entity.BriefModelVO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelUserBindVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8329376488522772766L;

    private OtherUserVO user;
    private BriefModelVO model;
}
