package com.psm.domain.Model.models_user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.models_user.repository.Models_UserDB;
import com.psm.domain.Model.models_user.valueObject.Models_UserDAO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.valueObject.ExtendedUserDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.infrastructure.DB.ModelMapper;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Slf4j
@Repository
public class Models_UserDBImpl implements Models_UserDB {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public Page<Models_UserDAO> selectModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId) {
        // 按照页配置获取发过模型的用户的ID列表,并按时间降序排序
        MPJLambdaWrapper<UserDAO> userMPJWrapper = new MPJLambdaWrapper<>();

        // 判断是否传入用户自己的ID,若传入，则表示筛选查找用户自己的关注目标用户
        boolean isFollowing = Objects.nonNull(userSelfId);

        // 当userSelfId不为null时，才拼接tb_followers表
        if (isFollowing) {
            userMPJWrapper.innerJoin(FollowerDAO.class, FollowerDAO::getTgtUserId, UserDAO::getId)
                // 筛选出关注目标用户的模型
                .and(wrapper -> wrapper.eq( FollowerDAO::getSrcUserId, userSelfId ));
        }

        // 当筛选条件涉及到模型表的字段时才拼接模型表
        boolean hasStyle = Objects.nonNull(style);
        boolean hasType = Objects.nonNull(type);
        if (hasStyle || hasType) {
            userMPJWrapper.innerJoin(ModelDAO.class, ModelDAO::getUserId, UserDAO::getId)

                // 筛选出符合样式和类型的模型
                .and(hasStyle, wrapper -> wrapper.eq( ModelDAO::getStyle, style))

                // 筛选出符合类型的模型
                .and(hasType, wrapper -> wrapper.eq( ModelDAO::getType, type));
        }

        userMPJWrapper
            // 筛选出可见的模型
            .and(Objects.nonNull(isIdle), wrapper -> wrapper.eq(UserDAO::getIsIdle, isIdle))

            // 筛选出可以加急接单的用户
            .and(Objects.nonNull(canUrgent), wrapper -> wrapper.eq(UserDAO::getCanUrgent, canUrgent))

            // 筛选出至少有一个公开模型的用户
            .and(wrapper -> wrapper.ge(UserDAO::getPublicModelNum, 1));

        // 仅选择用户ID和createTime字段(存在联合索引),避免回表查询
        userMPJWrapper.select(UserDAO::getId, UserDAO::getCreateTime);

        // 按照时间降序排序
        userMPJWrapper.orderByDesc(UserDAO::getCreateTime);

        // 因为用户id和模型存在一对多的关系，拼接后的查询结果存在id重复的问题，需要去重
        userMPJWrapper.distinct();

        // 按页数据获取用户列表
        Page<UserDAO> page = new Page<>(current, size);
        Page<UserDAO> userDAOPage = userMapper.selectPage(page, userMPJWrapper);

        // 按照用户DAO页获取发过模型的用户的ID列表,并按时间降序排序
        List<UserDAO> userDAOs = userDAOPage.getRecords();

        // 创建一个Map，用于存储用户ID和ModelsShowBarDAO的映射
        Map<Long, Models_UserDAO> collect = new HashMap<>();
        userDAOs.forEach(userExtensionDAO -> collect.putIfAbsent(userExtensionDAO.getId(), null));

        // 获取用户ID列表，这个ID列表是按照时间降序排序的
        List<Long> userIds = userDAOs.stream().map(UserDAO::getId).toList();

        // 如果用户ID列表为空，则直接返回一个空的Page对象
        if(userIds.isEmpty()) return new Page<>();

        // 按照用户ID列表获取用户列表
        LambdaQueryWrapper<UserDAO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.in(UserDAO::getId, userIds);
        List<UserDAO> userDAOList = userMapper.selectList(userWrapper);

        // 使每个用户id都对应一个ModelsShowBarDAO
        userDAOList.forEach(userDAO -> {
            collect.put(userDAO.getId(), new Models_UserDAO(userDAO, new ArrayList<>()));
        });

        // 按照用户ID列表获取作品模型列表
        LambdaQueryWrapper<ModelDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.in(ModelDAO::getUserId, userIds);
        modelWrapper.eq(ModelDAO::getVisible, VisibleEnum.PUBLIC);
        // 筛选出符合样式和类型的模型
        modelWrapper.and(hasStyle, wrapper -> wrapper.eq( ModelDAO::getStyle, style));

        // 筛选出符合类型的模型
        modelWrapper.and(hasType, wrapper -> wrapper.eq( ModelDAO::getType, type));
        modelWrapper.select(ModelDAO::getId, ModelDAO::getUserId, ModelDAO::getTitle, ModelDAO::getCover,
                ModelDAO::getStyle, ModelDAO::getType);
        List<ModelDAO> modelDAOList = modelMapper.selectList(modelWrapper);

        // 将作品模型列表添加到对应的ModelsShowBarDAO中
        modelDAOList.forEach(modelDAO -> {
            Models_UserDAO modelsShowBarDAO = collect.get(modelDAO.getUserId());
            modelsShowBarDAO.getModels().add(modelDAO);
        });

        // 复制需要返回的页信息
        List<Models_UserDAO> modelsUserBindDAOs = collect.values().stream().toList();
        Page<Models_UserDAO> models_UserDAOPage = new Page<>();
        BeanUtils.copyProperties(userDAOPage, models_UserDAOPage);
        models_UserDAOPage.setRecords(modelsUserBindDAOs);

        return models_UserDAOPage;
    }
}
