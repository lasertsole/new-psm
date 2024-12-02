package com.psm.domain.Chat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Chat.types.convertor.ChatConvertor;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.entity.User.UserDODefine;
import com.psm.types.common.DO.DO;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
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
public class ChatDO implements Serializable, DO<ChatBO> {
    @ColumnId(comment = "id主键")
    private Long id;

    @Index(name = "tb_chats_tgtUserId_index")
    @Column(comment = "目标用户id", notNull = true)
    private Long tgtUserId;

    @Index(name = "tb_chats_srcUserId_index")
    @Column(comment = "来源用户id", notNull = true)
    private Long srcUserId;

    @Index(name = "tb_chats_timestamp_index")
    @Column(comment = "信息时间戳（UTC时间）")
    private String timestamp;

    @Column(comment = "消息内容")
    private String content;

    @BindEntity(conditions = @JoinCondition(selfField = ChatDODefine.tgtUserId, joinField = UserDODefine.id))
    private UserDO tgtUser;

    @BindEntity(conditions = @JoinCondition(selfField = ChatDODefine.srcUserId, joinField = UserDODefine.id))
    private UserDO srcUser;

    public static ChatDO fromBO(ChatBO chatBO) {
        return ChatConvertor.INSTANCE.BO2DO(chatBO);
    }

    @Override
    public ChatBO toBO() {
        return ChatConvertor.INSTANCE.DO2BO(this);
    }
}