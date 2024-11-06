package com.psm.domain.User.follower.types.convertor;

import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.domain.User.follower.valueObject.ExtendedUserDAO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.psm.domain.User.user.types.enums.SexEnum;

import java.util.Optional;

@Mapper
public abstract class ExtendedUserConvertor {

    public static final ExtendedUserConvertor INSTANCE = Mappers.getMapper(ExtendedUserConvertor.class);

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public ExtendedUserBO DAO2BO(ExtendedUserDAO extendedUserDAO) {
        return new ExtendedUserBO(
            extendedUserDAO.getId(),
            extendedUserDAO.getName(),
            extendedUserDAO.getPassword(),
            extendedUserDAO.getPhone(),
            extendedUserDAO.getAvatar(),
            extendedUserDAO.getEmail(),
            extendedUserDAO.getSex(),
            extendedUserDAO.getProfile(),
            extendedUserDAO.getCreateTime(),
            extendedUserDAO.getModifyTime(),
            extendedUserDAO.getIsFollowed()
        );
    };

    public ExtendedUserVO BO2OtherVO(ExtendedUserBO extendedUserBO) {
        return new ExtendedUserVO(
            Optional.ofNullable(extendedUserBO.getId()).map(Object::toString).orElse(null),
            extendedUserBO.getName(),
            null,
            null,
            extendedUserBO.getAvatar(),
            null,
            Optional.ofNullable(extendedUserBO.getSex()).map(SexEnum::getValue).orElse(null),
            extendedUserBO.getProfile(),
            extendedUserBO.getCreateTime(),
            extendedUserBO.getIsFollowed()
        );
    };
}
