package com.psm.domain.Chat.entity;

import com.psm.utils.VO.DTO2VOable;
import com.psm.domain.Chat.types.convertor.ChatConvertor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO implements DTO2VOable<ChatVO>, Serializable {
    private Long id;
    private Long tgtUserId;
    private Long srcUserId;
    private String timestamp;
    private String content;

    public static ChatDTO fromBO(ChatBO chatBO) {
        return ChatConvertor.INSTANCE.BO2DTO(chatBO);
    }

    @Override
    public ChatVO toVO() {
        return ChatConvertor.INSTANCE.DTO2VO(this);
    }
}
