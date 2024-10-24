package com.psm.domain.ModelsShowBar.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.Model.repository.mapper.ModelMapper;
import com.psm.domain.ModelsShowBar.repository.ModelsShowBarDB;
import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarDAO;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.repository.mapper.UserExtensionMapper;
import com.psm.domain.User.repository.mapper.UserMapper;
import com.psm.infrastructure.annotation.spring.Repository;
import com.psm.infrastructure.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ModelsShowBarDBImpl implements ModelsShowBarDB {
    @Autowired
    private UserExtensionMapper userExtensionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ModelMapper modelMapper;

    public List<ModelsShowBarDAO> selectModelsShowBarOrderByCreateTimeDesc(Integer currentPage, Integer pageSize) {
        // 按照页配置获取发过模型的用户的ID列表,并按时间降序排序
        LambdaQueryWrapper<UserExtensionDAO> userExtensionWrapper = new LambdaQueryWrapper<>();
        userExtensionWrapper.select(UserExtensionDAO::getId);
        userExtensionWrapper.gt(UserExtensionDAO::getPublicModelNum, 0);
        userExtensionWrapper.orderByDesc(UserExtensionDAO::getCreateTime);
        Page<UserExtensionDAO> page = new Page<>(currentPage, pageSize);
        Page<UserExtensionDAO> UserExtensionDAOResultPage = userExtensionMapper.selectPage(page, userExtensionWrapper);
        List<UserExtensionDAO> UserExtensionDAOResultList = UserExtensionDAOResultPage.getRecords();

        // 如果用户列表为空，则返回空列表
        if (UserExtensionDAOResultList.isEmpty()) return Collections.emptyList();

        // 创建一个Map，用于存储用户ID和ModelsShowBarDAO的映射
        Map<Long, ModelsShowBarDAO> collect = UserExtensionDAOResultList.stream().collect(Collectors.toMap(
                UserExtensionDAO::getId, userExtensionDAO -> new ModelsShowBarDAO()
        ));

        // 获取用户ID列表，这个ID列表是按照时间降序排序的
        List<Long> userIds = UserExtensionDAOResultList.stream().map(UserExtensionDAO::getId).toList();

        // 按照用户ID列表获取用户列表
        LambdaQueryWrapper<UserDAO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.in(UserDAO::getId, userIds);
        List<UserDAO> userDAOList = userMapper.selectList(userWrapper);
        userDAOList.stream().forEach(userDAO -> {
            ModelsShowBarDAO modelsShowBarDAO = collect.get(userDAO.getId());
            modelsShowBarDAO.setUser(userDAO);
            modelsShowBarDAO.setModels(new ArrayList<ModelDAO>());
        });

        // 按照用户ID列表获取作品模型列表
        LambdaQueryWrapper<ModelDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.in(ModelDAO::getUserId, userIds);
        modelWrapper.eq(ModelDAO::getVisible, VisibleEnum.PUBLIC);
        modelWrapper.select(ModelDAO::getId, ModelDAO::getUserId, ModelDAO::getTitle, ModelDAO::getCover, ModelDAO::getCategory,
                ModelDAO::getCreateTime);
        List<ModelDAO> modelDAOList = modelMapper.selectList(modelWrapper);

        modelDAOList.forEach(modelDAO -> {
            ModelsShowBarDAO modelsShowBarDAO = collect.get(modelDAO.getUserId());
            modelsShowBarDAO.getModels().add(modelDAO);
        });

        // 返回结果
        return collect.values().stream().toList();
    }
}
