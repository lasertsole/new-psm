package com.psm.domain.Model.infrastructure.convertor;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.Model.entity.ModelBO;
import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.Model.entity.ModelVO;
import com.psm.domain.Model.valueObject.Category;
import com.psm.infrastructure.enums.VisibleEnum;
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

    @Named("fromString")
    protected Category fromBoolean(String value) {
        Category category = JSON.parseObject(value, Category.class);
        return category;
    }

    @Named("fromInteger")
    public VisibleEnum fromShort(Integer visable) {
        return VisibleEnum.fromInteger(visable);
    }

    @Mappings({
            @Mapping(source = "cover", target = "cover", ignore = true),
            @Mapping(target = "category", qualifiedByName = "fromString"),
            @Mapping(target = "visible", qualifiedByName = "fromInteger")
    })
    public abstract ModelDAO DTO2DAO(ModelDTO modelDTO);

    @Mappings({
            @Mapping(target = "cover", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "visible", qualifiedByName = "fromInteger")
    })
    public abstract ModelBO DTO2BO(ModelDTO modelDTO);

    public abstract ModelBO DAO2BO(ModelDAO modelDAO);

    @Named("longToString")
    public String longToString(Long num) {
        return num.toString();
    }

    @Mappings({
            @Mapping(source = "visible.value", target = "visible"),
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "userId", qualifiedByName = "longToString")
    })
    public abstract ModelVO BO2VO(ModelBO modelBO);
}
