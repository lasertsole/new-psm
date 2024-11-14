package com.psm.domain.Chat.types.convertor;

import com.psm.domain.Chat.entity.ChatBO;
import com.psm.domain.Chat.entity.ChatDAO;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.Chat.entity.ChatVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ChatConvertor {
    public static ChatConvertor INSTANCE = Mappers.getMapper(ChatConvertor.class);

    public abstract ChatDAO DTO2DAO(ChatDTO chatDAO);

    public abstract ChatBO DAO2BO(ChatDAO chatDAO);

    public abstract ChatVO BO2VO(ChatBO chatBO);
}
