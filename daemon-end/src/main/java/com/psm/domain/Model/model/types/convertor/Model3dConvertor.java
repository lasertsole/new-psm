package com.psm.domain.Model.model.types.convertor;

import com.psm.domain.Model.model.entity.Model3dDO;
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
            @Mapping(target = "cover", ignore = true),
            @Mapping(target = "visible", qualifiedByName = "fromInteger"),
            @Mapping(target = "storage", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "modifyTime", ignore = true)
    })
    public abstract Model3dBO DTO2BO(Model3dDTO model3dDTO);

    @Mappings({
        @Mapping(target = "deleted", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    public abstract Model3dDO BO2DO(Model3dBO model3dBO);

    @Mappings({
        @Mapping(target = "coverFile", ignore = true)
    })
    public abstract Model3dBO DO2BO(Model3dDO model3dDO);

    @Mappings({
        @Mapping(target = "cover", ignore = true),
        @Mapping(source = "visible.value", target = "visible", defaultExpression = "java(null)"),
        @Mapping(target = "coverFile", ignore = true)
    })
    public abstract Model3dDTO BO2DTO(Model3dBO model3dBO);

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
            @Mapping(source = "visible", target = "visible", defaultExpression = "java(null)"),
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "userId", qualifiedByName = "longToString"),
    })
    public abstract Model3dVO DTO2VO(Model3dDTO model3dDTO);
}
