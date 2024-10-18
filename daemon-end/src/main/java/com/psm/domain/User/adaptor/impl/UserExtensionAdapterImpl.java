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
    public void insert(UserExtensionBO userExtensionBO) {
        insert(UserExtensionConvertor.INSTANCE.BO2DTO(userExtensionBO));
    }

    @Override
    public UserExtensionBO selectById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        UserExtensionDAO userExtensionDAO = userExtensionService.selectById(userExtensionDTO.getId());

        return UserExtensionConvertor.INSTANCE.DAO2BO(userExtensionDAO);
    }

    @Override
    public UserExtensionBO selectById(UserExtensionBO userExtensionBO) {
        return selectById(UserExtensionConvertor.INSTANCE.BO2DTO(userExtensionBO));
    }

    @Override
    public boolean updateById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                || (
                        ObjectUtils.isNull(userExtensionDTO.getModelNum())
                )
        )
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.updateById(userExtensionDTO);
    }

    @Override
    public boolean updateById(UserExtensionBO userExtensionBO) {
        return updateById(UserExtensionConvertor.INSTANCE.BO2DTO(userExtensionBO));
    }

    @Override
    public UserExtensionDAO selectWorkNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.selectWorkNumById(userExtensionDTO.getId());
    }

    @Override
    public UserExtensionDAO selectWorkNumById(UserExtensionBO userExtensionBO) {
        return selectWorkNumById(UserExtensionConvertor.INSTANCE.BO2DTO(userExtensionBO));
    }


    @Override
    public boolean updateWorkNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                        && ObjectUtils.isNull(userExtensionDTO.getModelNum())
        )
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.updateModelNumById(userExtensionDTO.getId(), userExtensionDTO.getModelNum());
    }

    @Override
    public boolean updateWorkNumById(UserExtensionBO userExtensionBO) {
        return updateWorkNumById(UserExtensionConvertor.INSTANCE.BO2DTO(userExtensionBO));
    }

    @Override
    public boolean addOneModelNumById(Long id) {
        return userExtensionService.addOneModelNumById(id);
    }

    @Override
    public boolean addOneModelNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return addOneModelNumById(userExtensionDTO.getId());
    }

    @Override
    public boolean removeOneModelNumById(Long id) {
        return userExtensionService.removeOneModelNumById(id);
    }

    @Override
    public boolean removeOneModelNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return removeOneModelNumById(userExtensionDTO.getId());
    }

    @Override
    public Long updateOneModelStorageById(Long id, Long storage) {
        return userExtensionService.updateOneModelStorageById(id, storage);
    }

    @Override
    public Long updateOneModelStorageById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                && ObjectUtils.isNull(userExtensionDTO.getModelCurStorage())
        )
            throw new InvalidParameterException("Invalid parameter");

        return updateOneModelStorageById(userExtensionDTO.getId(), userExtensionDTO.getModelCurStorage());
    }

    @Override
    public Long addOneModelStorageById(Long id, Long storage) {
        return userExtensionService.addOneModelStorageById(id, storage);
    }

    @Override
    public Long addOneModelStorageById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                        && ObjectUtils.isNull(userExtensionDTO.getModelCurStorage())
        )
            throw new InvalidParameterException("Invalid parameter");

        return addOneModelStorageById(userExtensionDTO.getId(), userExtensionDTO.getModelCurStorage());
    }

    @Override
    public Long minusOneModelStorageById(Long id, Long storage) {
        return userExtensionService.minusOneModelStorageById(id, storage);
    }

    @Override
    public Long minusOneModelStorageById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                && ObjectUtils.isNull(userExtensionDTO.getModelCurStorage())
        )
            throw new InvalidParameterException("Invalid parameter");

        return minusOneModelStorageById(userExtensionDTO.getId(), userExtensionDTO.getModelCurStorage());
    }

    @Override
    public Long addOneModelById(Long id, Long storage) {
        addOneModelNumById(id);
        return addOneModelStorageById(id, storage);
    }

    @Override
    public Long removeOneModelById(Long id, Long storage) {
        removeOneModelNumById(id);
        return minusOneModelStorageById(id, storage);
    }
}
