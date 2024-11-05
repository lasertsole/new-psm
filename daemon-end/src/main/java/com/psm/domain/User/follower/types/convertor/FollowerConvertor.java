package com.psm.domain.User.follower.types.convertor;

import com.psm.domain.User.follower.entity.FollowerBO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.entity.FollowerDTO;
import com.psm.domain.User.follower.entity.FollowerVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class FollowerConvertor {

    public static final FollowerConvertor INSTANCE = Mappers.getMapper(FollowerConvertor.class);

    public abstract FollowerDTO BO2DTO(FollowerBO followerBO);

    public abstract FollowerDAO DTO2DAO(FollowerDTO followerDTO);

    public abstract FollowerBO DAO2BO(FollowerDAO followerDAO);

    @Named("longToString")
    public String longToString(Long num) {
        return num.toString();
    }

    @Mappings({
            @Mapping(target = "id", qualifiedByName = "longToString"),
            @Mapping(target = "tgtUserId", qualifiedByName = "longToString"),
            @Mapping(target = "srcUserId", qualifiedByName = "longToString")
    })
    public abstract FollowerVO BO2VO(FollowerBO followerBO);
}
