package com.psm.domain.User.user.types.convertor;

import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.domain.User.user.types.enums.SexEnum;
import com.psm.domain.User.user.event.bus.security.utils.BcryptEncoderUtil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public abstract class UserConvertor {

    public static final UserConvertor INSTANCE = Mappers.getMapper(UserConvertor.class);

    @Named("fromBoolean")
    protected SexEnum fromBoolean(Boolean value) {return SexEnum.fromBoolean(value);}

    @Mappings({
        @Mapping(source = "avatar", target = "avatar", ignore = true),
        @Mapping(target = "sex", qualifiedByName = "fromBoolean"),
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "modifyTime", ignore = true),
        @Mapping(target = "token", ignore = true),
        @Mapping(target = "id", qualifiedByName = "stringToLong"),
        @Mapping(target = "modelMaxStorage", qualifiedByName = "stringToLong"),
        @Mapping(target = "modelCurStorage", qualifiedByName = "stringToLong"),
    })
    public abstract UserBO DTO2BO(UserDTO userDTO);

    @Mappings({
        @Mapping(target = "deleted", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    public abstract UserDO BO2DO(UserBO userBO);

    @Mappings({
        @Mapping(target = "token", ignore = true),
        @Mapping(target = "changePassword", ignore = true),
        @Mapping(target = "avatarFile", ignore = true),
        @Mapping(target = "oldAvatar", ignore = true)
    })
    public abstract UserBO DO2BO(UserDO userDO);

    @Mappings({
            @Mapping(source = "sex.value", target = "sex"),
            @Mapping(target = "hasPass", ignore = true),
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "modelMaxStorage", ignore = true),
            @Mapping(target = "modelCurStorage", ignore = true)
    })
    public abstract UserDTO DO2OtherDTO(UserDO userDO);

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
            @Mapping(source = "sex.value", target = "sex"),
            @Mapping(target = "hasPass", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "modelMaxStorage", ignore = true),
            @Mapping(target = "modelCurStorage", ignore = true)
    })
    public abstract UserDTO BO2OtherDTO(UserBO userBO);

    @Mappings({
            @Mapping(source = "sex.value", target = "sex"),
            @Mapping(source = "password", target = "hasPass", qualifiedByName = "processPass"),
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "modelMaxStorage", qualifiedByName = "longToString"),
            @Mapping(target = "modelCurStorage", qualifiedByName = "longToString")
    })
    public abstract UserDTO BO2CurrentDTO(UserBO userBO);

    @Named("processPass")
    protected Boolean processPass(String password) {
        return BcryptEncoderUtil.isBcrypt(password);
    }
}
