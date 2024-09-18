package com.psm.domain.User.infrastructure.Convertor;

import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class UserConvertor {
    public static UserConvertor instance = Mappers.getMapper(UserConvertor.class);

    @Mappings(
            @Mapping(source = "avatar", target = "avatar", ignore = true)
    )
    public abstract UserDAO DTO2DAO(UserDTO userDTO);

    public abstract UserBO DAO2BO(UserDAO userDAO);
}
