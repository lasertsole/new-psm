package com.psm.domain.User.user.infrastructure.convertor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class UserExtensionConvertor {
    public static final UserExtensionConvertor INSTANCE = Mappers.getMapper(UserExtensionConvertor.class);

    public abstract UserExtensionDAO DTO2DAO(UserExtensionDTO userExtensionDTO);

    public abstract UserExtensionBO DAO2BO(UserExtensionDAO userExtensionDAO);

    public abstract UserExtensionDTO BO2DTO(UserExtensionBO userExtensionBO);
}
