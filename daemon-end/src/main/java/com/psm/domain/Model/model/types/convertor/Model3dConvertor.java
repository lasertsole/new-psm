package com.psm.domain.Model.model.types.convertor;

import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.model.entity.Model3dVO;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Slf4j
@Mapper
public abstract class Model3dConvertor {
    public static final Model3dConvertor INSTANCE = Mappers.getMapper(Model3dConvertor.class);

    @Named("fromInteger")
    public VisibleEnum fromShort(Integer visible) {
        try{
            return VisibleEnum.fromInteger(visible);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Mappings({
            @Mapping(source = "cover", target = "cover", ignore = true),
            @Mapping(target = "visible", qualifiedByName = "fromInteger")
    })
    public abstract Model3dDAO DTO2DAO(Model3dDTO model3dDTO);

    @Mappings({
            @Mapping(target = "cover", ignore = true),
            @Mapping(target = "visible", qualifiedByName = "fromInteger")
    })
    public abstract Model3dBO DTO2BO(Model3dDTO model3dDTO);

    public abstract Model3dBO DAO2BO(Model3dDAO model3dDAO);

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
    public abstract Model3dVO BO2VO(Model3dBO model3dBO);
}
