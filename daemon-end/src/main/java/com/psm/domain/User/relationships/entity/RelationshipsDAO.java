package com.psm.domain.User.relationships.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.annotation.InsertFillTime;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_relationships", comment="关注表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsDAO implements Serializable {
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
}