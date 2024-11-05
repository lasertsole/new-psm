package com.psm.domain.User.user.adaptor.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.adaptor.UserExtensionAdapter;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDTO;
import com.psm.domain.User.user.types.convertor.UserExtensionConvertor;
import com.psm.domain.User.user.service.UserExtensionService;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.types.utils.page.PageDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
import java.util.List;

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
                        ObjectUtils.isNull(userExtensionDTO.getPublicModelNum())
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
    public boolean updateModelNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (
                ObjectUtils.isNull(userExtensionDTO.getId())
                        && ObjectUtils.isNull(userExtensionDTO.getPublicModelNum())
        )
            throw new InvalidParameterException("Invalid parameter");

        return userExtensionService.updateModelNumById(userExtensionDTO.getId(), userExtensionDTO.getPublicModelNum());
    }

    @Override
    public boolean updateModelNumById(UserExtensionBO userExtensionBO) {
        return updateModelNumById(UserExtensionConvertor.INSTANCE.BO2DTO(userExtensionBO));
    }

    @Override
    public boolean addOnePublicModelNumById(Long id) {
        return userExtensionService.addOneModelNumById(id);
    }

    @Override
    public boolean addOnePublicModelNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return addOnePublicModelNumById(userExtensionDTO.getId());
    }

    @Override
    public boolean removeOnePublicModelNumById(Long id) {
        return userExtensionService.removeOneModelNumById(id);
    }

    @Override
    public boolean removeOnePublicModelNumById(@Valid UserExtensionDTO userExtensionDTO) {
        if (ObjectUtils.isNull(userExtensionDTO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return removeOnePublicModelNumById(userExtensionDTO.getId());
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
    public Page<UserExtensionBO> getHasPublicModelOrderByCreateTimeDesc(@Valid PageDTO pageDTO) {
        if (
                org.springframework.util.ObjectUtils.isEmpty(pageDTO.getCurrent())
                && org.springframework.util.ObjectUtils.isEmpty(pageDTO.getSize())
        )
            throw new InvalidParameterException("Invalid parameter");

        // 获取UserExtensionBO列表
        Page<UserExtensionDAO> hasPublicModelOrderByCreateTimeDesc = userExtensionService.getHasPublicModelOrderByCreateTimeDesc(pageDTO.getCurrent(), pageDTO.getSize());
        UserExtensionConvertor userExtensionConvertor = UserExtensionConvertor.INSTANCE;
        List<UserExtensionBO> UserExtensionBOs = hasPublicModelOrderByCreateTimeDesc.getRecords().stream().map(userExtensionConvertor::DAO2BO).toList();

        // 将DAO转换为BO
        Page<UserExtensionBO> userExtensionBOPage = new Page<>();
        BeanUtil.copyProperties(hasPublicModelOrderByCreateTimeDesc, userExtensionBOPage);
        userExtensionBOPage.setRecords(UserExtensionBOs);
        return userExtensionBOPage;
    }
}
