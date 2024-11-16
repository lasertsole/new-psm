package com.psm.domain.User.relationships.types.convertor;

import com.psm.domain.User.relationships.entity.RelationshipsBO;
import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.domain.User.relationships.entity.RelationshipsDTO;
import com.psm.domain.User.relationships.entity.RelationshipsVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class RelationshipsConvertor {

    public static final RelationshipsConvertor INSTANCE = Mappers.getMapper(RelationshipsConvertor.class);

    public abstract RelationshipsDTO BO2DTO(RelationshipsBO followerBO);

    public abstract RelationshipsDAO DTO2DAO(RelationshipsDTO followerDTO);

    public abstract RelationshipsBO DAO2BO(RelationshipsDAO followerDAO);

    @Named("longToString")
    public String longToString(Long num) {
        return num.toString();
    }

    @Mappings({
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
            @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract RelationshipsVO BO2VO(RelationshipsBO followerBO);
}
