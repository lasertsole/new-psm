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

    @Named("fromShort")
    public VisibleEnum fromShort(Integer visable) {
        switch (visable) {
            case 0:
                return VisibleEnum.PRIVATE;
            case 1:
                return VisibleEnum.PROTECTED;
            case 2:
                return VisibleEnum.PUBLIC;
            default:
                return VisibleEnum.PRIVATE;
        }
    }

    @Mappings({
            @Mapping(source = "cover", target = "cover", ignore = true),
            @Mapping(target = "category", qualifiedByName = "fromString"),
            @Mapping(target = "visible", qualifiedByName = "fromShort")
    })
    public abstract ModelDAO DTO2DAO(ModelDTO modelDTO);

    @Mappings({
            @Mapping(target = "cover", ignore = true),
            @Mapping(target = "category", ignore = true)
    })
    public abstract ModelBO DTO2BO(ModelDTO modelDTO);

    @Mappings({
            @Mapping(source = "visible.visible", target = "visible")
    })
    public abstract ModelBO DAO2BO(ModelDAO modelDAO);

    public abstract ModelVO BO2VO(ModelBO modelBO);
}
