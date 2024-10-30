package com.psm.domain.User.user.infrastructure.convertor;

import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.domain.User.user.entity.User.UserVO.CurrentUserVO;
import com.psm.domain.User.user.entity.User.UserVO.OtherUserVO;
import com.psm.domain.User.user.infrastructure.enums.SexEnum;
import com.psm.domain.User.user.infrastructure.utils.BcryptEncoderUtil;
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
            @Mapping(source = "sex.value", target = "sex")
    })
    public abstract UserBO DAO2BO(UserDAO userDAO);

    @Named("longToString")
    public String longToString(Long num) {
        return num.toString();
    }

    @Mappings({
            @Mapping(target = "id", qualifiedByName = "longToString")
    })
    public abstract OtherUserVO BO2VO(UserBO userBO);

    @Mappings({
            @Mapping(target = "hasPass", ignore = true),
            @Mapping(target = "id", qualifiedByName = "longToString")
    })
    public abstract CurrentUserVO BO2CurrentVO(UserBO userBO);

    @AfterMapping
    protected void afterBO2CurrentVO(@MappingTarget CurrentUserVO currentUserVO, UserBO userBO) {
        currentUserVO.setHasPass(BcryptEncoderUtil.isBcrypt(userBO.getPassword()));
    }
}
