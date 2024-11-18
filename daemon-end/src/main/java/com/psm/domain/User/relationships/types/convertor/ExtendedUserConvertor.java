package com.psm.domain.User.relationships.types.convertor;

import com.psm.domain.User.relationships.valueObject.ExtendedUserBO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDTO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserVO;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.psm.domain.User.user.types.enums.SexEnum;

import java.util.Optional;

@Mapper
public abstract class ExtendedUserConvertor {

    public static final ExtendedUserConvertor INSTANCE = Mappers.getMapper(ExtendedUserConvertor.class);

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public ExtendedUserBO DTO2BO(ExtendedUserDTO extendedUserDTO) {
        return new ExtendedUserBO(
                extendedUserDTO.getId(),
                extendedUserDTO.getName(),
                extendedUserDTO.getPassword(),
                extendedUserDTO.getPhone(),
                extendedUserDTO.getAvatar(),
                extendedUserDTO.getEmail(),
                SexEnum.fromBoolean(extendedUserDTO.getSex()),
                extendedUserDTO.getProfile(),
                extendedUserDTO.getPublicModelNum(),
                extendedUserDTO.getModelMaxStorage(),
                extendedUserDTO.getModelCurStorage(),
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

    public ExtendedUserDTO BO2DTO(ExtendedUserBO extendedUserBO) {
        return new ExtendedUserDTO(
                extendedUserBO.getId(),
                extendedUserBO.getName(),
                extendedUserBO.getPassword(),
                extendedUserBO.getPhone(),
                extendedUserBO.getAvatar(),
                extendedUserBO.getEmail(),
                Optional.ofNullable(extendedUserBO.getSex()).map(SexEnum::getValue).orElse(null),
                extendedUserBO.getProfile(),
                extendedUserBO.getPublicModelNum(),
                extendedUserBO.getModelMaxStorage(),
                extendedUserBO.getModelCurStorage(),
                extendedUserBO.getIsIdle(),
                extendedUserBO.getCanUrgent(),
                extendedUserBO.getCreateTime(),
                extendedUserBO.getIsFollowed()
        );
    }

    public ExtendedUserVO DTO2VO(ExtendedUserDTO extendedUserDTO) {
        return new ExtendedUserVO(
                Optional.ofNullable(extendedUserDTO.getId()).map(Object::toString).orElse(null),
                extendedUserDTO.getName(),
                null,
                null,
                extendedUserDTO.getAvatar(),
                null,
                extendedUserDTO.getSex(),
                extendedUserDTO.getProfile(),
                extendedUserDTO.getPublicModelNum(),
                null,
                null,
                extendedUserDTO.getIsIdle(),
                extendedUserDTO.getCanUrgent(),
                extendedUserDTO.getCreateTime(),
                extendedUserDTO.getIsFollowed()
        );
    }
}