package com.psm.domain.User.infrastructure.Convertor;

import com.psm.domain.User.entity.User.*;
import com.psm.domain.User.entity.User.UserVO.CurrentUserVO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import com.psm.domain.User.infrastructure.enums.SexEnum;
import com.psm.domain.User.infrastructure.utils.BcryptEncoderUtil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserConvertor {
    public static final UserConvertor INSTANCE = Mappers.getMapper(UserConvertor.class);

    @Named("fromBoolean")
    protected SexEnum fromBoolean(boolean value) {
        return value ? SexEnum.FEMALE : SexEnum.MALE;
    }

    @Mappings({
            @Mapping(source = "avatar", target = "avatar", ignore = true),
            @Mapping(target = "sex", qualifiedByName = "fromBoolean")
    })
    public abstract UserDAO DTO2DAO(UserDTO userDTO);

    @Mappings({
            @Mapping(source = "sex.sex", target = "sex")
    })
    public abstract UserBO DAO2BO(UserDAO userDAO);

    public abstract OtherUserVO BO2VO(UserBO userBO);

    public abstract CurrentUserVO BO2CurrentVO(UserBO userBO);

    @AfterMapping
    protected void afterBO2CurrentVO(@MappingTarget CurrentUserVO currentUserVO, UserBO userBO) {
        currentUserVO.setHasPass(BcryptEncoderUtil.isBcrypt(userBO.getPassword()));
    }
}
