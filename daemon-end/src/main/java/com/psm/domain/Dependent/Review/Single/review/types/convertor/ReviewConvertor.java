package com.psm.domain.Dependent.Review.Single.review.types.convertor;

import com.psm.domain.Dependent.Review.Single.review.entity.ReviewBO;
import com.psm.domain.Dependent.Review.Single.review.entity.ReviewDO;
import com.psm.domain.Dependent.Review.Single.review.entity.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public abstract class ReviewConvertor {

    public static final ReviewConvertor INSTANCE = Mappers.getMapper(ReviewConvertor.class);

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
        @Mapping(target = "srcUserId", qualifiedByName = "stringToLong"),
        @Mapping(target = "targetId", qualifiedByName = "stringToLong")
    })
    public abstract ReviewBO DTO2BO(ReviewDTO reviewDTO);

    public abstract ReviewDO BO2DO(ReviewBO reviewBO);

    public abstract ReviewBO DO2BO(ReviewDO reviewDO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString"),
        @Mapping(target = "targetId", qualifiedByName = "longToString"),
        @Mapping(target = "attachUserId", qualifiedByName = "longToString"),
        @Mapping(target = "replyUserId", qualifiedByName = "longToString"),
    })
    public abstract ReviewDTO DO2DTO(ReviewDO reviewDO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString"),
        @Mapping(target = "targetId", qualifiedByName = "longToString"),
        @Mapping(target = "attachUserId", qualifiedByName = "longToString"),
        @Mapping(target = "replyUserId", qualifiedByName = "longToString"),
    })
    public abstract ReviewDTO BO2DTO(ReviewBO reviewBO);
}
