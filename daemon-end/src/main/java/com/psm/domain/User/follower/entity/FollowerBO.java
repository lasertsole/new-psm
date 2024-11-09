package com.psm.domain.User.follower.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.follower.types.convertor.FollowerConvertor;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.User.UserDAODefine;
import com.psm.utils.VO.BO2VOable;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowerBO implements BO2VOable<FollowerVO>, Serializable {

    private Long id;

    private Long tgtUserId;
    private Long srcUserId;

    private String createTime;

    @BindEntity(conditions = @JoinCondition(selfField = FollowerDAODefine.tgtUserId, joinField = UserDAODefine.id))
    private UserDAO tgtUser;

    @BindEntity(conditions = @JoinCondition(selfField = FollowerDAODefine.srcUserId, joinField = UserDAODefine.id))
    private UserDAO srcUser;

    @Override
    public FollowerVO toVO() {
        return FollowerConvertor.INSTANCE.BO2VO(this);
    }
}
