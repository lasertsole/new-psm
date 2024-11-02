package com.psm.domain.User.follower.infrastructure.convertor;

import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import com.psm.domain.User.user.entity.User.UserVO;
import com.psm.domain.User.user.infrastructure.convertor.UserConvertor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ExtendedUserConvertor {

    public static final ExtendedUserConvertor INSTANCE = Mappers.getMapper(ExtendedUserConvertor.class);

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;
    public ExtendedUserVO BO2VO(ExtendedUserBO extendedUserBO) {
        UserVO userVO = userConvertor.BO2OtherVO(extendedUserBO.getUser());

        return new ExtendedUserVO(userVO, extendedUserBO.getFollowed());
    };
}
