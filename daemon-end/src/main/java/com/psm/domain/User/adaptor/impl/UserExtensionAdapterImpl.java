package com.psm.domain.User.adaptor.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.psm.domain.User.adaptor.UserExtensionAdapter;
import com.psm.domain.User.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDTO;
import com.psm.domain.User.infrastructure.convertor.UserExtensionConvertor;
import com.psm.domain.User.service.UserExtensionService;
import com.psm.infrastructure.annotation.spring.Adaptor;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;

@Slf4j
@Adaptor
public class UserExtensionAdapterImpl implements UserExtensionAdapter {
    @Autowired
    private UserExtensionService userExtensionService;

    @Override
    public void insert(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        userExtensionService.insert(userExtensionDTO.getId());
    }

    @Override
    public UserExtensionBO selectById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        UserExtensionDAO userExtensionDAO = userExtensionService.selectById(userExtensionDTO.getId());

        return UserExtensionConvertor.INSTANCE.DAO2BO(userExtensionDAO);
    }

    @Override
    public boolean updateById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                || (
                        ObjectUtils.isNull(userExtensionDTO.getWork_num())
                )
        )
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.updateById(userExtensionDTO);
    }

    @Override
    public short selectWorkNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.selectWorkNumById(userExtensionDTO.getId());
    }

    @Override
    public boolean updateWorkNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                && ObjectUtils.isNull(userExtensionDTO.getWork_num())
        )
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.updateWorkNumById(userExtensionDTO.getId(), userExtensionDTO.getWork_num());
    }

    @Override
    public boolean addOneWorkNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.addOneWorkNumById(userExtensionDTO.getId());
    }
}
