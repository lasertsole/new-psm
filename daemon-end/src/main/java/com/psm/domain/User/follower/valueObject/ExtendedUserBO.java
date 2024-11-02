package com.psm.domain.User.follower.valueObject;

import com.psm.domain.User.follower.infrastructure.convertor.ExtendedUserConvertor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
public class ExtendedUserBO implements BO2VOable<ExtendedUserVO>, Serializable {
    @Serial
    private static final long serialVersionUID = 5378942493144950433L;

    UserBO user;
    String followed;

    public ExtendedUserVO toVO() {
        return ExtendedUserConvertor.INSTANCE.BO2VO(this);
    }
}
