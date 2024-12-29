package com.psm.domain.Independent.User.Single.relationships.entity;

import com.psm.domain.Independent.User.Single.relationships.types.convertor.RelationshipsConvertor;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;
import com.psm.types.common.DO.DO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.annotation.InsertFillTime;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDODefine;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;

import java.io.Serializable;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_relationships", comment="关注表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsDO implements Serializable, DO<RelationshipsBO, RelationshipsDTO> {
    @ColumnId(comment = "id主键")
    private Long id;

    @Index(name = "relationships_tgtUserId_index")
    @Column(comment = "关注目标用户的id", notNull = true)
    private Long tgtUserId;

    @Index(name = "relationships_srcUserId_index")
    @Column(comment = "关注来源用户的id", notNull = true)
    private Long srcUserId;

    @Column(comment = "是否关注", defaultValue = "false", notNull = true)
    private Boolean isFollowing;

    @Column(comment = "是否在聊天列表内显示", defaultValue = "false", notNull = true)
    private Boolean isInContacts;

    @Column(comment = "是否屏蔽", defaultValue = "false", notNull = true)
    private Boolean isBlocking;

    @InsertFillTime
    @Column(comment = "创建时间")
    private String createTime;

    @BindEntity(conditions = @JoinCondition(selfField = RelationshipsDODefine.tgtUserId, joinField = UserDODefine.id))
    private UserDO tgtUser;

    @BindEntity(conditions = @JoinCondition(selfField = RelationshipsDODefine.srcUserId, joinField = UserDODefine.id))
    private UserDO srcUser;

    public static RelationshipsDO fromBO(RelationshipsBO relationshipsBO) {
        return RelationshipsConvertor.INSTANCE.BO2DO(relationshipsBO);
    }

    @Override
    public RelationshipsBO toBO() {
        return RelationshipsConvertor.INSTANCE.DO2BO(this);
    }

    @Override
    public RelationshipsDTO toDTO() {
        return RelationshipsConvertor.INSTANCE.DO2DTO(this);
    }
}