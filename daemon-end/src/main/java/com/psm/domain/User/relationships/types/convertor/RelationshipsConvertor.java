package com.psm.domain.User.relationships.types.convertor;

import com.psm.domain.User.relationships.entity.RelationshipsBO;
import com.psm.domain.User.relationships.entity.RelationshipsDO;
import com.psm.domain.User.relationships.entity.RelationshipsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class RelationshipsConvertor {

    public static final RelationshipsConvertor INSTANCE = Mappers.getMapper(RelationshipsConvertor.class);

    public abstract RelationshipsDO BO2DO(RelationshipsBO followerBO);

    @Mappings({
            @Mapping(target = "createTime", ignore = true),
    })
    public abstract RelationshipsBO DTO2BO(RelationshipsDTO followerDTO);

    public abstract RelationshipsBO DO2BO(RelationshipsDO followerDO);

    @Mappings({
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
            @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract RelationshipsDTO DO2DTO(RelationshipsDO followerDO);

    @Mappings({
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
            @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract RelationshipsDTO BO2DTO(RelationshipsBO followerBO);

    @Named("longToString")
    public String longToString(Long num) {
        return num.toString();
    }
}
