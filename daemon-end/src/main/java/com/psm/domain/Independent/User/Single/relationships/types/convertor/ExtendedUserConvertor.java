package com.psm.domain.Independent.User.Single.relationships.types.convertor;

import com.psm.domain.Independent.User.Single.relationships.valueObject.ExtendedUserBO;
import com.psm.domain.Independent.User.Single.relationships.valueObject.ExtendedUserDO;
import com.psm.domain.Independent.User.Single.relationships.valueObject.ExtendedUserDTO;
import com.psm.domain.Independent.User.Single.user.types.convertor.UserConvertor;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import com.psm.domain.Independent.User.Single.user.types.enums.SexEnum;

import java.util.Objects;
import java.util.Optional;

@Mapper
public abstract class ExtendedUserConvertor {

    public static final ExtendedUserConvertor INSTANCE = Mappers.getMapper(ExtendedUserConvertor.class);

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

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

    public ExtendedUserBO DTO2BO(ExtendedUserDTO extendedUserDTO) {
        return new ExtendedUserBO(
                stringToLong(extendedUserDTO.getId()),
                extendedUserDTO.getName(),
                extendedUserDTO.getPassword(),
                extendedUserDTO.getPhone(),
                extendedUserDTO.getAvatar(),
                extendedUserDTO.getEmail(),
                SexEnum.fromBoolean(extendedUserDTO.getSex()),
                extendedUserDTO.getProfile(),
                extendedUserDTO.getPublicModelNum(),
                stringToLong(extendedUserDTO.getModelMaxStorage()),
                stringToLong(extendedUserDTO.getModelCurStorage()),
                extendedUserDTO.getIsIdle(),
                extendedUserDTO.getCanUrgent(),
                extendedUserDTO.getCreateTime(),
                null,
                extendedUserDTO.getIsFollowed()
        );
    };

    public ExtendedUserBO DO2BO(ExtendedUserDO extendedUserDO) {
        return new ExtendedUserBO(
                extendedUserDO.getId(),
                extendedUserDO.getName(),
                extendedUserDO.getPassword(),
                extendedUserDO.getPhone(),
                extendedUserDO.getAvatar(),
                extendedUserDO.getEmail(),
                extendedUserDO.getSex(),
                extendedUserDO.getProfile(),
                extendedUserDO.getPublicModelNum(),
                extendedUserDO.getModelMaxStorage(),
                extendedUserDO.getModelCurStorage(),
                extendedUserDO.getIsIdle(),
                extendedUserDO.getCanUrgent(),
                extendedUserDO.getCreateTime(),
                extendedUserDO.getModifyTime(),
                extendedUserDO.getIsFollowed()
        );
    };

    public ExtendedUserDTO DO2DTO(ExtendedUserDO extendedUserDO) {
        return new ExtendedUserDTO(
                longToString(extendedUserDO.getId()),
                extendedUserDO.getName(),
                extendedUserDO.getPassword(),
                extendedUserDO.getPhone(),
                extendedUserDO.getAvatar(),
                extendedUserDO.getEmail(),
                Optional.ofNullable(extendedUserDO.getSex()).map(SexEnum::getValue).orElse(null),
                extendedUserDO.getProfile(),
                extendedUserDO.getPublicModelNum(),
                null,
                null,
                extendedUserDO.getIsIdle(),
                extendedUserDO.getCanUrgent(),
                extendedUserDO.getCreateTime(),
                extendedUserDO.getIsFollowed()
        );
    };

    public ExtendedUserDO BO2DO(ExtendedUserBO extendedUserBO) {
        return new ExtendedUserDO(
                extendedUserBO.getId(),
                extendedUserBO.getName(),
                extendedUserBO.getPassword(),
                extendedUserBO.getPhone(),
                extendedUserBO.getAvatar(),
                extendedUserBO.getEmail(),
                extendedUserBO.getSex(),
                extendedUserBO.getProfile(),
                extendedUserBO.getPublicModelNum(),
                extendedUserBO.getModelMaxStorage(),
                extendedUserBO.getModelCurStorage(),
                extendedUserBO.getIsIdle(),
                extendedUserBO.getCanUrgent(),
                extendedUserBO.getCreateTime(),
                extendedUserBO.getModifyTime(),
                null,
                null,
                extendedUserBO.getIsFollowed()
        );
    };

    public ExtendedUserDTO BO2DTO(ExtendedUserBO extendedUserBO) {
        return new ExtendedUserDTO(
                longToString(extendedUserBO.getId()),
                extendedUserBO.getName(),
                null,
                null,
                extendedUserBO.getAvatar(),
                null,
                Optional.ofNullable(extendedUserBO.getSex()).map(SexEnum::getValue).orElse(null),
                extendedUserBO.getProfile(),
                extendedUserBO.getPublicModelNum(),
                null,
                null,
                extendedUserBO.getIsIdle(),
                extendedUserBO.getCanUrgent(),
                extendedUserBO.getCreateTime(),
                extendedUserBO.getIsFollowed()
        );
    }
}