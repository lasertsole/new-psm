package com.psm.domain.User.infrastructure.convertor;

import com.psm.domain.User.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserExtensionConvertor {
    public static final UserExtensionConvertor INSTANCE = Mappers.getMapper(UserExtensionConvertor.class);

    public abstract UserExtensionDAO DTO2DAO(UserExtensionDTO userExtensionDTO);

    public abstract UserExtensionBO DAO2BO(UserExtensionDAO userExtensionDAO);

    public abstract UserExtensionDTO BO2DTO(UserExtensionBO userExtensionBO);
}
