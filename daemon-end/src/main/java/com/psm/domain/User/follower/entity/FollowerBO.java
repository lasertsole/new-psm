package com.psm.domain.User.follower.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowerBO implements BO2VOable<FollowerVO>, Serializable {
    @Serial
    private static final long serialVersionUID = 2921186381260417021L;

    private Long id;

    private Long tgtUserId;
    private Long srcUserId;

    private String createTime;

    @Override
    public FollowerVO toVO() {
        return null;
    }
}
