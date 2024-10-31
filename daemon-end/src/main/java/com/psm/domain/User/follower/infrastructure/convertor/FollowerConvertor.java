package com.psm.domain.User.follower.infrastructure.convertor;

import com.psm.domain.User.follower.entity.FollowerBO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.entity.FollowerDTO;
import com.psm.domain.User.follower.entity.FollowerVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class FollowerConvertor {

    public static final FollowerConvertor INSTANCE = Mappers.getMapper(FollowerConvertor.class);

    public abstract FollowerDTO BO2DTO(FollowerBO followerBO);

    public abstract FollowerDAO DTO2DAO(FollowerDTO followerDTO);

    public abstract FollowerBO DAO2BO(FollowerDAO followerDAO);

    public abstract FollowerVO BO2VO(FollowerBO followerBO);
}
