package com.psm.domain.Model.models_user.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.Model.models_user.entity.ModelUserDAO;
import com.psm.domain.Model.models_user.repository.Models_UserDB;
import com.psm.domain.Model.models_user.valueObject.Models_UserDAO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.infrastructure.DB.Model3dMapper;
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
    private Model3dMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

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
        boolean hasIsIdle = Objects.nonNull(isIdle);
        boolean hasCanUrgent = Objects.nonNull(canUrgent);

        if (hasStyle || hasType) {
            userMPJWrapper.innerJoin(Model3dDAO.class, Model3dDAO::getUserId, UserDAO::getId)

                // 筛选出符合样式和类型的模型
                .and(hasStyle, wrapper -> wrapper.eq( Model3dDAO::getStyle, style))

                // 筛选出符合类型的模型
                .and(hasType, wrapper -> wrapper.eq( Model3dDAO::getType, type));
        }

        userMPJWrapper
            // 筛选出可见的模型
            .and(hasIsIdle, wrapper -> wrapper.eq(UserDAO::getIsIdle, isIdle))

            // 筛选出可以加急接单的用户
            .and(hasCanUrgent, wrapper -> wrapper.eq(UserDAO::getCanUrgent, canUrgent))

            // 筛选出至少有一个公开模型的用户
            .and(wrapper -> wrapper.ge(UserDAO::getPublicModelNum, 1));

        // 仅选择用户ID和createTime字段(存在联合索引),避免回表查询
        userMPJWrapper.select(UserDAO::getId);

        // 按照时间降序排序(因为使用雪花算法，id自带顺序，从小到大)
        userMPJWrapper.orderByDesc(UserDAO::getId);

        // 因为用户id和模型存在一对多的关系，拼接后的查询结果存在id重复的问题，需要去重
        userMPJWrapper.distinct();

        // 按页数据获取用户列表
        Page<UserDAO> userDAOPage = new Page<>(current, size);
        userMapper.selectPage(userDAOPage, userMPJWrapper);

        // 如果用户ID列表为空，则直接返回一个空的Page对象
        if (userDAOPage.getRecords().isEmpty()) {
            Page<Models_UserDAO> page = new Page<>();
            BeanUtils.copyProperties(userDAOPage, page);
            return page;
        }

        // 按照用户DAO页获取发过模型的用户的ID列表,并按时间降序排序
        List<Long> ids = userDAOPage.getRecords().stream().map(UserDAO::getId).toList();

        // 按照用户ID获取用户-模型一对多列表
        MPJLambdaWrapper<UserDAO> userJoinModelWrapper = new MPJLambdaWrapper<UserDAO>();//主表为用户表
        userJoinModelWrapper.innerJoin(Model3dDAO.class, Model3dDAO::getUserId, UserDAO::getId);//拼接从表3d模型表,连接条件为userDAO.Id = Model3dDAO.userId
        userJoinModelWrapper.selectAs(Model3dDAO::getId, ModelUserDAO::getModelId);
        userJoinModelWrapper.select(Model3dDAO::getTitle, Model3dDAO::getCover,
                Model3dDAO::getStyle, Model3dDAO::getType);
        userJoinModelWrapper.selectAs(UserDAO::getId, ModelUserDAO::getUserId);
        userJoinModelWrapper.select(UserDAO::getName, UserDAO::getAvatar, UserDAO::getSex, UserDAO::getProfile,
                UserDAO::getPublicModelNum, UserDAO::getIsIdle, UserDAO::getCanUrgent, UserDAO::getCreateTime);
        userJoinModelWrapper.eq(Model3dDAO::getVisible, VisibleEnum.PUBLIC);

        userJoinModelWrapper.eq(hasStyle, Model3dDAO::getStyle, style);
        userJoinModelWrapper.eq(hasType, Model3dDAO::getType, type);

        userJoinModelWrapper.eq(hasIsIdle, UserDAO::getIsIdle, isIdle);
        userJoinModelWrapper.eq(hasCanUrgent, UserDAO::getCanUrgent, canUrgent);

        userJoinModelWrapper.in(UserDAO::getId, ids);

        // 按照用户ID和模型ID进行关联查询
        List<ModelUserDAO> modelUserDAOs = userMapper.selectJoinList(ModelUserDAO.class, userJoinModelWrapper);


        Map<Long, Models_UserDAO> userMap = new HashMap<>();

        modelUserDAOs.forEach(
                modelUserDAO -> {
                    Model3dDAO model = new Model3dDAO(
                            modelUserDAO.getModelId(),
                            null,
                            modelUserDAO.getTitle(),
                            null,
                            modelUserDAO.getCover(),
                            null,
                            null,
                            null,
                            modelUserDAO.getStyle(),
                            modelUserDAO.getType(),
                            modelUserDAO.getCreateTime(),
                            null,
                            null,
                            null
                    );

                    if (!userMap.containsKey(modelUserDAO.getUserId())){
                        UserDAO user = new UserDAO(
                                modelUserDAO.getUserId(),
                                modelUserDAO.getName(),
                                null,
                                null,
                                modelUserDAO.getAvatar(),
                                null,
                                modelUserDAO.getSex(),
                                modelUserDAO.getProfile(),
                                modelUserDAO.getPublicModelNum(),
                                null,
                                null,
                                modelUserDAO.getIsIdle(),
                                modelUserDAO.getCanUrgent(),
                                null,
                                null,
                                null,
                                null
                        );

                        List<Model3dDAO> models = new ArrayList<>();
                        models.add(model);
                        Models_UserDAO models_UserDAO = new Models_UserDAO(user, models);
                        userMap.put(modelUserDAO.getUserId(), models_UserDAO);
                    }
                    else{
                        userMap.get(modelUserDAO.getUserId()).getModels().add(model);
                    }
                }
        );

        List<Models_UserDAO> values = new ArrayList<>(userMap.values());
        Page<Models_UserDAO> page = new Page<>();
        BeanUtils.copyProperties(userDAOPage, page);
        page.setRecords(values);

        return page;
    }
}
