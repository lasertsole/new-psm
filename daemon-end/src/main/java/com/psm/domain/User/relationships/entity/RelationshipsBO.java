package com.psm.domain.User.relationships.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.relationships.types.convertor.RelationshipsConvertor;
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
public class RelationshipsBO implements BO2VOable<RelationshipsVO>, Serializable {

    private Long id;

    private Long tgtUserId;
    private Long srcUserId;
    private Boolean isFollowing;
    private Boolean isInContacts;
    private Boolean isBlocking;

    private String createTime;

    @BindEntity(conditions = @JoinCondition(selfField = RelationshipsDAODefine.tgtUserId, joinField = UserDAODefine.id))
    private UserDAO tgtUser;

    @BindEntity(conditions = @JoinCondition(selfField = RelationshipsDAODefine.srcUserId, joinField = UserDAODefine.id))
    private UserDAO srcUser;

    @Override
    public RelationshipsVO toVO() {
        return RelationshipsConvertor.INSTANCE.BO2VO(this);
    }
}
