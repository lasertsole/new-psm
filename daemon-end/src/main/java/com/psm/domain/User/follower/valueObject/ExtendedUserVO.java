package com.psm.domain.User.follower.valueObject;

import com.psm.domain.User.user.entity.User.UserVO;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
public class ExtendedUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7517841450890343992L;

    UserVO user;
    String followed;
}
