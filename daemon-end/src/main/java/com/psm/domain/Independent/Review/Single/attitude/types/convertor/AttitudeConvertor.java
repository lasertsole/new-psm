package com.psm.domain.Independent.Review.Single.attitude.types.convertor;

import com.psm.domain.Independent.Review.Single.attitude.pojo.entity.AttitudeDO;
import com.psm.domain.Independent.Review.Single.attitude.pojo.entity.AttitudeBO;
import com.psm.domain.Independent.Review.Single.attitude.pojo.entity.AttitudeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public abstract class AttitudeConvertor {
    public static final AttitudeConvertor INSTANCE = Mappers.getMapper(AttitudeConvertor.class);

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
        @Mapping(target = "targetId", qualifiedByName = "stringToLong"),
    })
    public abstract AttitudeBO DTO2BO(AttitudeDTO attitudeDTO);

    public abstract AttitudeDO BO2DO(AttitudeBO attitudeBO);

    public abstract AttitudeBO DO2BO(AttitudeDO attitudeDO);


    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString"),
        @Mapping(target = "targetId", qualifiedByName = "longToString"),
    })
    public abstract AttitudeDTO DO2DTO(AttitudeDO attitudeDO);

    @Mappings({
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "srcUserId", qualifiedByName = "longToString"),
        @Mapping(target = "targetId", qualifiedByName = "longToString"),
    })
    public abstract AttitudeDTO BO2DTO(AttitudeBO attitudeBO);
}
