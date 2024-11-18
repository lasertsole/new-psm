package com.psm.domain.Chat.entity;

import com.psm.domain.Chat.types.convertor.ChatConvertor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import com.psm.utils.VO.DTO2VOable;
import com.psm.utils.Valid.ValidUtil;
import com.tangzc.mpe.bind.Binder;
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

    private String content;

    public static ChatBO fromDTO(ChatDTO chatDTO) {
        return ChatConvertor.INSTANCE.DTO2BO(chatDTO);
    }

    public static ChatBO fromDO(ChatDO chatDO) {
        return ChatConvertor.INSTANCE.DO2BO(chatDO);
    }
}
