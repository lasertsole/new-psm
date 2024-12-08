package com.psm.domain.Communication.Chat.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Communication.Chat.types.convertor.ChatConvertor;

import java.time.format.DateTimeFormatter;

import com.psm.types.common.BO.BO;
import com.psm.utils.Timestamp.TimestampUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBO implements Serializable, BO<ChatDTO, ChatDO> {
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The tgtUserId must be greater than or equal to 1")
    private Long tgtUserId;

    @Min(value = 1, message = "The srcUserId must be greater than or equal to 1")
    private Long srcUserId;

    private String timestamp;

    private String content;

    public static ChatBO fromDTO(ChatDTO chatDTO) {
        return ChatConvertor.INSTANCE.DTO2BO(chatDTO);
    }

    public static ChatBO fromDO(ChatDO chatDO) {
        return ChatConvertor.INSTANCE.DO2BO(chatDO);
    }

    public static Page<ChatBO> fromDOPage(Page<ChatDO> chatDOPage) {
        Page<ChatBO> chatBOPage = new Page<>();
        BeanUtils.copyProperties(chatDOPage, chatBOPage);
        chatBOPage.setRecords(chatDOPage.getRecords().stream().map(ChatConvertor.INSTANCE::DO2BO).toList());

        return chatBOPage;
    }

    public String generateTimestamp() {
        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String timestamp = TimestampUtils.generateUTCTimestamp(formatter);

        // ChatBO的timestamp属性设置时间戳
        this.timestamp = timestamp;
        return timestamp;
    }

    @Override
    public ChatDTO toDTO() {
        return ChatConvertor.INSTANCE.BO2DTO(this);
    }

    @Override
    public ChatDO toDO() {
        return ChatConvertor.INSTANCE.BO2DO(this);
    }
}
