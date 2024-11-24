package com.psm.domain.Chat.types.convertor;

import com.psm.domain.Chat.entity.ChatBO;
import com.psm.domain.Chat.entity.ChatDO;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.Chat.entity.ChatVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public abstract class ChatConvertor {
    public static ChatConvertor INSTANCE = Mappers.getMapper(ChatConvertor.class);

    @Named("longToString")
    public String longToString(Long num) {
        if (Objects.isNull(num)) return null;
        return num.toString();
    }

    @Named("stringToLong")
    public Long stringToLong(String str) {
        if (Objects.isNull(str) || str.isEmpty()) return null;
        return Long.parseLong(str);
    }
    @Mappings({
        @Mapping(target = "id", qualifiedByName = "stringToLong"),
        @Mapping(target = "tgtUserId", qualifiedByName = "stringToLong"),
        @Mapping(target = "srcUserId", qualifiedByName = "stringToLong")
    })
    public abstract ChatBO DTO2BO(ChatDTO chatDO);

    @Mappings({
        @Mapping(target = "tgtUser", ignore = true),
        @Mapping(target = "srcUser", ignore = true),
    })
    public abstract ChatDO BO2DO(ChatBO chatBO);

    public abstract ChatBO DO2BO(ChatDO chatDO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract ChatDTO BO2DTO(ChatBO chatBO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract ChatDTO DO2DTO(ChatDO chatDO);

    public abstract ChatVO DTO2VO(ChatDTO chatDTO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract ChatVO BO2VO(ChatBO chatBO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract ChatVO DO2VO(ChatDO chatDO);
}
