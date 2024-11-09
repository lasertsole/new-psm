package com.psm.domain.User.follower.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@Table(value = "tb_followers", comment="关注表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowerDAO implements Serializable {
    @ColumnId(comment = "id主键")
    private Long id;

    @Index(name = "tgtUserId_index")
    @Column(comment = "关注目标用户的id")
    private Long tgtUserId;

    @Index(name = "srcUserId_index")
    @Column(comment = "关注来源用户的id")
    private Long srcUserId;

    @InsertFillTime
    @Column(comment = "创建时间")
    private String createTime;
}