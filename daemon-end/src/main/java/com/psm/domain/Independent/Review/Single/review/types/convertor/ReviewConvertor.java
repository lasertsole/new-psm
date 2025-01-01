package com.psm.domain.Independent.Review.Single.review.types.convertor;

import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewBO;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewDO;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
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

    @Named("DOs2BOs")
    public List<ReviewBO> DOs2BOs(List<ReviewDO> reviewDOs) {
        if (Objects.isNull(reviewDOs) || reviewDOs.isEmpty()) return null;
        return reviewDOs.stream().map(this::DO2BO).toList();
    }

    @Named("DOs2DTOs")
    public List<ReviewDTO> DOs2DTOs(List<ReviewDO> reviewDOs) {
        if (Objects.isNull(reviewDOs) || reviewDOs.isEmpty()) return null;
        return reviewDOs.stream().map(this::DO2DTO).toList();
    }

    @Named("BOs2DTOs")
    public List<ReviewDTO> BOs2DTOs(List<ReviewBO> reviewBOs) {
        if (Objects.isNull(reviewBOs) || reviewBOs.isEmpty()) return null;
        return reviewBOs.stream().map(this::BO2DTO).toList();
    }

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "stringToLong"),
        @Mapping(target = "srcUserId", qualifiedByName = "stringToLong"),
        @Mapping(target = "targetId", qualifiedByName = "stringToLong"),
        @Mapping(target = "attaches", ignore = true),
        @Mapping(target = "replies", ignore = true),
    })
    public abstract ReviewBO DTO2BO(ReviewDTO reviewDTO);

    @Mappings({
        @Mapping(target = "attaches", ignore = true),
        @Mapping(target = "replies", ignore = true),
    })
    public abstract ReviewDO BO2DO(ReviewBO reviewBO);


    @Mappings({
            @Mapping(target = "attaches", qualifiedByName = "DOs2BOs"),
            @Mapping(target = "replies", qualifiedByName = "DOs2BOs"),
    })
    public abstract ReviewBO DO2BO(ReviewDO reviewDO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString"),
        @Mapping(target = "targetId", qualifiedByName = "longToString"),
        @Mapping(target = "attachId", qualifiedByName = "longToString"),
        @Mapping(target = "replyId", qualifiedByName = "longToString"),
        @Mapping(target = "attaches", qualifiedByName = "DOs2DTOs"),
        @Mapping(target = "replies", qualifiedByName = "DOs2DTOs"),
    })
    public abstract ReviewDTO DO2DTO(ReviewDO reviewDO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString"),
        @Mapping(target = "targetId", qualifiedByName = "longToString"),
        @Mapping(target = "attachId", qualifiedByName = "longToString"),
        @Mapping(target = "replyId", qualifiedByName = "longToString"),
        @Mapping(target = "attaches", qualifiedByName = "BOs2DTOs"),
        @Mapping(target = "replies", qualifiedByName = "BOs2DTOs"),
    })
    public abstract ReviewDTO BO2DTO(ReviewBO reviewBO);
}
