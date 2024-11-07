package com.psm.domain.Model.modelsUserBind.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.modelsUserBind.repository.ModelsUserBindDB;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindDAO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.infrastructure.DB.ModelMapper;
import com.psm.infrastructure.DB.UserExtensionMapper;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Slf4j
@Repository
public class ModelsUserBindDBImpl implements ModelsUserBindDB {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserExtensionMapper userExtensionMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public Page<ModelsUserBindDAO> selectModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId) {
        // 按照页配置获取发过模型的用户的ID列表,并按时间降序排序
        MPJLambdaWrapper<UserExtensionDAO> userExtensionWrapper = new MPJLambdaWrapper<>();

        // 当userSelfId不为null时，才拼接tb_followers表
        if (Objects.nonNull(userSelfId)) {
            userExtensionWrapper.innerJoin(FollowerDAO.class, FollowerDAO::getTgtUserId, UserExtensionDAO::getId)
                // 筛选出关注目标用户的模型
                .and(wrapper -> wrapper.eq( FollowerDAO::getSrcUserId, userSelfId ));
        }

        // 当筛选条件涉及到模型表的字段时才拼接模型表
        if (Objects.nonNull(style) || Objects.nonNull(type)) {
            userExtensionWrapper.innerJoin(ModelDAO.class, ModelDAO::getUserId, UserExtensionDAO::getId)

                // 筛选出符合样式和类型的模型
                .and(Objects.nonNull(style), wrapper -> wrapper.eq( ModelDAO::getStyle, style))

                // 筛选出符合类型的模型
                .and(Objects.nonNull(type), wrapper -> wrapper.eq( ModelDAO::getType, type));
        }

        userExtensionWrapper
            // 筛选出可见的模型
            .and(Objects.nonNull(isIdle), wrapper -> wrapper.eq(UserExtensionDAO::getIsIdle, isIdle))

            // 筛选出可以加急接单的用户
            .and(Objects.nonNull(canUrgent), wrapper -> wrapper.eq(UserExtensionDAO::getCanUrgent, canUrgent))

            // 筛选出至少有一个公开模型的用户
            .and(wrapper -> wrapper.ge(UserExtensionDAO::getPublicModelNum, 1));

        // 仅选择用户ID
        userExtensionWrapper.select(UserExtensionDAO::getId);

        // 按照时间降序排序
        userExtensionWrapper.orderByDesc(UserExtensionDAO::getCreateTime);

        // 按页数据获取用户列表
        Page<UserExtensionDAO> page = new Page<>(current, size);
        Page<UserExtensionDAO> userExtensionDAOPage = userExtensionMapper.selectPage(page, userExtensionWrapper);

        // 按照用户DAO页获取发过模型的用户的ID列表,并按时间降序排序
        List<UserExtensionDAO> userExtensionDAOs = userExtensionDAOPage.getRecords();

        // 创建一个Map，用于存储用户ID和ModelsShowBarDAO的映射
        Map<Long, ModelsUserBindDAO> collect = new HashMap<>();
        userExtensionDAOs.forEach(userExtensionDAO -> collect.putIfAbsent(userExtensionDAO.getId(), null));

        // 获取用户ID列表，这个ID列表是按照时间降序排序的
        List<Long> userIds = userExtensionDAOs.stream().map(UserExtensionDAO::getId).toList();

        // 如果用户ID列表为空，则直接返回一个空的Page对象
        if(userIds.isEmpty()) return new Page<>();

        // 按照用户ID列表获取用户列表
        LambdaQueryWrapper<UserDAO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.in(UserDAO::getId, userIds);
        List<UserDAO> userDAOList = userMapper.selectList(userWrapper);

        // 使每个用户id都对应一个ModelsShowBarDAO
        userDAOList.forEach(userDAO -> {
            collect.put(userDAO.getId(), new ModelsUserBindDAO(userDAO, new ArrayList<>()));
        });

        // 按照用户ID列表获取作品模型列表
        LambdaQueryWrapper<ModelDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.in(ModelDAO::getUserId, userIds);
        modelWrapper.eq(ModelDAO::getVisible, VisibleEnum.PUBLIC);
        modelWrapper.select(ModelDAO::getId, ModelDAO::getUserId, ModelDAO::getTitle, ModelDAO::getCover,
                ModelDAO::getStyle, ModelDAO::getType, ModelDAO::getCreateTime);
        List<ModelDAO> modelDAOList = modelMapper.selectList(modelWrapper);

        // 将作品模型列表添加到对应的ModelsShowBarDAO中
        modelDAOList.forEach(modelDAO -> {
            ModelsUserBindDAO modelsShowBarDAO = collect.get(modelDAO.getUserId());
            modelsShowBarDAO.getModels().add(modelDAO);
        });

        // 复制需要返回的页信息
        List<ModelsUserBindDAO> modelsUserBindDAOs = collect.values().stream().toList();
        Page<ModelsUserBindDAO> modelsUserBindDAOPage = new Page<>();
        BeanUtils.copyProperties(userExtensionDAOPage, modelsUserBindDAOPage);
        modelsUserBindDAOPage.setRecords(modelsUserBindDAOs);

        return modelsUserBindDAOPage;
    }
}
