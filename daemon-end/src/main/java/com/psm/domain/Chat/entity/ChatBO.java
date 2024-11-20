package com.psm.domain.Chat.entity;

import com.psm.domain.Chat.types.convertor.ChatConvertor;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBO implements Serializable {
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

    public String generateTimestamp() {
        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String timestamp = now.format(formatter);

        // ChatBO的timestamp属性设置时间戳
        this.timestamp = timestamp;
        return timestamp;
    }
}
