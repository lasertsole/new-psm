package com.psm.domain.User.follower.types.convertor;

import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ExtendedUserConvertor {

    public static final ExtendedUserConvertor INSTANCE = Mappers.getMapper(ExtendedUserConvertor.class);

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;
    public ExtendedUserVO BO2OtherVO(ExtendedUserBO extendedUserBO) {
        return new ExtendedUserVO(
            extendedUserBO.getId().toString(),
            extendedUserBO.getName(),
            null,
            null,
            extendedUserBO.getAvatar(),
            null,
            extendedUserBO.getSex(),
            extendedUserBO.getProfile(),
            extendedUserBO.getCreateTime(),
            extendedUserBO.getIsFollowed()
        );
    };
}
