package com.psm.domain.Chat.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.utils.VO.DTO2VOable;
import com.psm.domain.Chat.types.convertor.ChatConvertor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO implements DTO2VOable<ChatVO>, Serializable {
    private String id;
    private String tgtUserId;
    private String srcUserId;
    private String timestamp;
    private String content;

    public static ChatDTO fromBO(ChatBO chatBO) {
        return ChatConvertor.INSTANCE.BO2DTO(chatBO);
    }
    public static Page<ChatDTO> fromBOPage(Page<ChatBO> chatBOPage) {
        Page<ChatDTO> chatDTOPage = new Page<>();
        BeanUtils.copyProperties(chatBOPage, chatDTOPage);
        chatDTOPage.setRecords(chatBOPage.getRecords().stream().map(ChatConvertor.INSTANCE::BO2DTO).toList());

        return chatDTOPage;
    }

    public static Page<ChatDTO> fromDOPage(Page<ChatDO> chatDOPage) {
        Page<ChatDTO> chatDTOPage = new Page<>();
        BeanUtils.copyProperties(chatDOPage, chatDTOPage);
        chatDTOPage.setRecords(chatDOPage.getRecords().stream().map(ChatConvertor.INSTANCE::DO2DTO).toList());

        return chatDTOPage;
    }

    @Override
    public ChatVO toVO() {
        return ChatConvertor.INSTANCE.DTO2VO(this);
    }
}
