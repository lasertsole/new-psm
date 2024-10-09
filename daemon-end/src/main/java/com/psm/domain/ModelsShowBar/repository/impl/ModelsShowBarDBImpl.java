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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

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
        userExtensionWrapper.gt(UserExtensionDAO::getWork_num, 0);
        userExtensionWrapper.orderByDesc(UserExtensionDAO::getCreateTime);
        Page<UserExtensionDAO> page = new Page<>(currentPage, pageSize);
        Page<UserExtensionDAO> UserExtensionDAOResultPage = userExtensionMapper.selectPage(page, userExtensionWrapper);
        List<UserExtensionDAO> UserExtensionDAOResultList = UserExtensionDAOResultPage.getRecords();

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
            modelsShowBarDAO.setUserDAO(userDAO);
            modelsShowBarDAO.setModelDAOs(new ArrayList<ModelDAO>());
        });

        // 按照用户ID列表获取作品模型列表
        LambdaQueryWrapper<ModelDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.in(ModelDAO::getUserId, userIds);
        List<ModelDAO> modelDAOList = modelMapper.selectList(modelWrapper);
        modelDAOList.stream().forEach(modelDAO -> {
            ModelsShowBarDAO modelsShowBarDAO = collect.get(modelDAO.getUserId());
            modelsShowBarDAO.getModelDAOs().add(modelDAO);
        });

        // 返回结果
        return collect.values().stream().toList();
    }
}
