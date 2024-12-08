package com.psm.domain.Communication.Chat.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Communication.Chat.types.convertor.ChatConvertor;
import com.psm.types.common.BO.BO;
import com.psm.types.common.DTO.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO implements Serializable, DTO {
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
    public BO toBO() {
        return ChatConvertor.INSTANCE.DTO2BO(this);
    }
}
