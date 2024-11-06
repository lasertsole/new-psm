package com.psm.domain.Model.model.types.convertor;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.Model.model.valueObject.Category;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Slf4j
@Mapper
public abstract class ModelConvertor {
    public static final ModelConvertor INSTANCE = Mappers.getMapper(ModelConvertor.class);

    @Named("fromInteger")
    public VisibleEnum fromShort(Integer visable) {
        try{
            return VisibleEnum.fromInteger(visable);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Mappings({
            @Mapping(source = "cover", target = "cover", ignore = true),
            @Mapping(target = "visible", qualifiedByName = "fromInteger")
    })
    public abstract ModelDAO DTO2DAO(ModelDTO modelDTO);

    @Mappings({
            @Mapping(target = "cover", ignore = true),
            @Mapping(target = "visible", qualifiedByName = "fromInteger")
    })
    public abstract ModelBO DTO2BO(ModelDTO modelDTO);

    public abstract ModelBO DAO2BO(ModelDAO modelDAO);

    @Named("longToString")
    public String longToString(Long num) {
        try {
            return num.toString();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Mappings({
            @Mapping(source = "visible.value", target = "visible", defaultExpression = "java(null)"),
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "userId", qualifiedByName = "longToString")
    })
    public abstract ModelVO BO2VO(ModelBO modelBO);
}
