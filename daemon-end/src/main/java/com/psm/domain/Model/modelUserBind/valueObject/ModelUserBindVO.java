package com.psm.domain.Model.modelUserBind.valueObject;

import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.User.user.entity.User.UserVO;
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

    private UserVO user;
    private ModelVO model;
}
