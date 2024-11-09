package com.psm.domain.Chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tangzc.autotable.annotation.Index;
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
@Table(value = "tb_chats", comment = "聊天记录表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDAO implements Serializable {
    @ColumnId(comment = "id主键")
    private Long id;

    @Index(name = "tb_chats_tgtUserId_index")
    @Column(comment = "目标用户id", notNull = true)
    private Long tgtUserId;

    @Index(name = "tb_chats_srcUserId_index")
    @Column(comment = "来源用户id", notNull = true)
    private Long srcUserId;
}
