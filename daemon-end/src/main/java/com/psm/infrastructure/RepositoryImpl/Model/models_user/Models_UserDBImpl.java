package com.psm.infrastructure.RepositoryImpl.Model.models_user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dDO;
import com.psm.domain.Independent.Model.Joint.models_user.entity.ModelUserDO;
import com.psm.domain.Independent.Model.Joint.models_user.repository.Models_UserDB;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserDO;
import com.psm.domain.Independent.User.Single.relationships.entity.RelationshipsDO;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;
import com.psm.infrastructure.DB.Model3dMapper;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;

@Slf4j
@Repository
public class Models_UserDBImpl implements Models_UserDB {
    @Autowired
    private Model3dMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Cacheable(value = "models_UserCache", key = "#current+'_'+#isIdle+'_'+#canUrgent+'_'+#style+'_'+#type+'_'+#userSelfId+'_'+#size")
    public Page<Models_UserDO> selectModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId) {
        // 按照页配置获取发过模型的用户的ID列表,并按时间降序排序
        MPJLambdaWrapper<UserDO> userMPJWrapper = new MPJLambdaWrapper<>();

        // 判断是否传入用户自己的ID,若传入，则表示筛选查找用户自己的关注目标用户
        boolean isFollowing = Objects.nonNull(userSelfId);

        // 当userSelfId不为null时，才拼接tb_followers表
        if (isFollowing) {
            userMPJWrapper.innerJoin(RelationshipsDO.class, RelationshipsDO::getTgtUserId, UserDO::getId)
                // 筛选出关注目标用户的模型
                .and(wrapper -> wrapper.eq( RelationshipsDO::getSrcUserId, userSelfId ))
                .and(wrapper -> wrapper.eq( RelationshipsDO::getIsFollowing, true ));
        }

        // 当筛选条件涉及到模型表的字段时才拼接模型表
        boolean hasStyle = Objects.nonNull(style);
        boolean hasType = Objects.nonNull(type);
        boolean hasIsIdle = Objects.nonNull(isIdle);
        boolean hasCanUrgent = Objects.nonNull(canUrgent);

        if (hasStyle || hasType) {
            userMPJWrapper.innerJoin(Model3dDO.class, Model3dDO::getUserId, UserDO::getId)

                // 筛选出符合样式和类型的模型
                .and(hasStyle, wrapper -> wrapper.eq( Model3dDO::getStyle, style))

                // 筛选出符合类型的模型
                .and(hasType, wrapper -> wrapper.eq( Model3dDO::getType, type));
        }

        userMPJWrapper
            // 筛选出可见的模型
            .and(hasIsIdle, wrapper -> wrapper.eq(UserDO::getIsIdle, isIdle))

            // 筛选出可以加急接单的用户
            .and(hasCanUrgent, wrapper -> wrapper.eq(UserDO::getCanUrgent, canUrgent))

            // 筛选出至少有一个公开模型的用户
            .and(wrapper -> wrapper.ge(UserDO::getPublicModelNum, 1));

        // 仅选择用户ID和createTime字段(存在联合索引),避免回表查询
        userMPJWrapper.select(UserDO::getId);

        // 按照时间降序排序(因为使用雪花算法，id自带顺序，从小到大)
        userMPJWrapper.orderByDesc(UserDO::getId);

        // 因为用户id和模型存在一对多的关系，拼接后的查询结果存在id重复的问题，需要去重
        userMPJWrapper.distinct();

        // 按页数据获取用户列表
        Page<UserDO> userDOPage = new Page<>(current, size);
        userMapper.selectPage(userDOPage, userMPJWrapper);

        // 如果用户ID列表为空，则直接返回一个空的Page对象
        if (userDOPage.getRecords().isEmpty()) {
            Page<Models_UserDO> page = new Page<>();
            BeanUtils.copyProperties(userDOPage, page);
            return page;
        }

        // 按照用户DO页获取发过模型的用户的ID列表,并按时间降序排序
        List<Long> ids = userDOPage.getRecords().stream().map(UserDO::getId).toList();

        // 按照用户ID获取用户-模型一对多列表
        MPJLambdaWrapper<UserDO> userJoinModelWrapper = new MPJLambdaWrapper<UserDO>();//主表为用户表
        userJoinModelWrapper.innerJoin(Model3dDO.class, Model3dDO::getUserId, UserDO::getId);//拼接从表3d模型表,连接条件为userDO.Id = Model3dDO.userId
        userJoinModelWrapper.selectAs(Model3dDO::getId, ModelUserDO::getModelId);
        userJoinModelWrapper.select(Model3dDO::getTitle, Model3dDO::getCover,
                Model3dDO::getStyle, Model3dDO::getType);
        userJoinModelWrapper.selectAs(UserDO::getId, ModelUserDO::getUserId);
        userJoinModelWrapper.select(UserDO::getName, UserDO::getAvatar, UserDO::getSex, UserDO::getProfile,
                UserDO::getPublicModelNum, UserDO::getIsIdle, UserDO::getCanUrgent, UserDO::getCreateTime);
        userJoinModelWrapper.eq(Model3dDO::getVisible, VisibleEnum.PUBLIC);

        userJoinModelWrapper.eq(hasStyle, Model3dDO::getStyle, style);
        userJoinModelWrapper.eq(hasType, Model3dDO::getType, type);

        userJoinModelWrapper.eq(hasIsIdle, UserDO::getIsIdle, isIdle);
        userJoinModelWrapper.eq(hasCanUrgent, UserDO::getCanUrgent, canUrgent);

        userJoinModelWrapper.in(UserDO::getId, ids);

        // 按照用户ID和模型ID进行关联查询
        List<ModelUserDO> modelUserDOs = userMapper.selectJoinList(ModelUserDO.class, userJoinModelWrapper);


        Map<Long, Models_UserDO> userMap = new HashMap<>();

        modelUserDOs.forEach(
                modelUserDO -> {
                    Model3dDO model = new Model3dDO(
                            modelUserDO.getModelId(),
                            null,
                            modelUserDO.getTitle(),
                            null,
                            modelUserDO.getCover(),
                            null,
                            null,
                            null,
                            modelUserDO.getStyle(),
                            modelUserDO.getType(),
                            modelUserDO.getCreateTime(),
                            null,
                            null,
                            null
                    );

                    if (!userMap.containsKey(modelUserDO.getUserId())){
                        UserDO user = new UserDO(
                                modelUserDO.getUserId(),
                                modelUserDO.getName(),
                                null,
                                null,
                                modelUserDO.getAvatar(),
                                null,
                                modelUserDO.getSex(),
                                modelUserDO.getProfile(),
                                modelUserDO.getPublicModelNum(),
                                null,
                                null,
                                modelUserDO.getIsIdle(),
                                modelUserDO.getCanUrgent(),
                                null,
                                null,
                                null,
                                null
                        );

                        List<Model3dDO> models = new ArrayList<>();
                        models.add(model);
                        Models_UserDO models_UserDO = new Models_UserDO(user, models);
                        userMap.put(modelUserDO.getUserId(), models_UserDO);
                    }
                    else{
                        userMap.get(modelUserDO.getUserId()).getModels().add(model);
                    }
                }
        );

        List<Models_UserDO> values = new ArrayList<>(userMap.values());
        Page<Models_UserDO> page = new Page<>();
        BeanUtils.copyProperties(userDOPage, page);
        page.setRecords(values);

        return page;
    }
}
