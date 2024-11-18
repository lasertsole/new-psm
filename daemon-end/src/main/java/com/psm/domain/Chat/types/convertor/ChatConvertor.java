package com.psm.domain.Chat.types.convertor;

import com.psm.domain.Chat.entity.ChatBO;
import com.psm.domain.Chat.entity.ChatDO;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.Chat.entity.ChatVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ChatConvertor {
    public static ChatConvertor INSTANCE = Mappers.getMapper(ChatConvertor.class);

    public abstract ChatBO DTO2BO(ChatDTO chatDO);

    public abstract ChatDO BO2DO(ChatBO chatBO);

    public abstract ChatBO DO2BO(ChatDO chatDO);

    public abstract ChatDTO BO2DTO(ChatBO chatBO);

    public abstract ChatVO DTO2VO(ChatDTO chatDTO);
}
