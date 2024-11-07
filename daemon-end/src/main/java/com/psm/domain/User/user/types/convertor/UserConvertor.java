package com.psm.domain.User.user.types.convertor;

import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.domain.User.user.entity.User.UserVO;
import com.psm.domain.User.user.types.enums.SexEnum;
import com.psm.domain.User.user.types.security.utils.BcryptEncoderUtil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public abstract class UserConvertor {

    public static final UserConvertor INSTANCE = Mappers.getMapper(UserConvertor.class);

    @Named("fromBoolean")
    protected SexEnum fromBoolean(Boolean value) {
        if (Objects.isNull(value)) return null;
        return value ? SexEnum.FEMALE : SexEnum.MALE;
    }

    @Mappings({
            @Mapping(source = "avatar", target = "avatar", ignore = true),
            @Mapping(target = "sex", qualifiedByName = "fromBoolean")
    })
    public abstract UserDAO DTO2DAO(UserDTO userDTO);

    public abstract UserBO DAO2BO(UserDAO userDAO);

    @Named("longToString")
    public String longToString(Long num) {
        if (Objects.isNull(num)) return null;
        return num.toString();
    }

    @Mappings({
        @Mapping(target = "hasPass", ignore = true),
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(target = "phone", ignore = true),
        @Mapping(target = "email", ignore = true),
        @Mapping(source = "sex.value", target = "sex", defaultExpression = "java(null)"),
        @Mapping(target = "modelMaxStorage", ignore = true),
        @Mapping(target = "modelCurStorage", ignore = true)
    })
    public abstract UserVO BO2OtherVO(UserBO userBO);

    @Mappings({
        @Mapping(source = "password", target = "hasPass", qualifiedByName = "processPass"),
        @Mapping(target = "id", qualifiedByName = "longToString"),
        @Mapping(source = "sex.value", target = "sex", defaultExpression = "java(null)")
    })
    public abstract UserVO BO2CurrentVO(UserBO userBO);

    @Named("processPass")
    protected Boolean processPass(String password) {
        return BcryptEncoderUtil.isBcrypt(password);
    }
}
