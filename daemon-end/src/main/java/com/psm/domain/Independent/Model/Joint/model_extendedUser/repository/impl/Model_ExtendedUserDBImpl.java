package com.psm.domain.Independent.Model.Joint.model_extendedUser.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Model.Single.model.entity.Model3dDO;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.repository.Model_ExtendedUserDB;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.valueObject.Model_ExtendedUserDO;
import com.psm.domain.Independent.User.Single.relationships.entity.RelationshipsDO;
import com.psm.domain.Independent.User.Single.relationships.valueObject.ExtendedUserDO;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;
import com.psm.infrastructure.DB.RelationshipsMapper;
import com.psm.infrastructure.DB.Model3dMapper;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Slf4j
@Repository
public class Model_ExtendedUserDBImpl implements Model_ExtendedUserDB {
    @Autowired
    Model3dMapper model3dMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RelationshipsMapper relationshipsMapperMapper;

    @Override
    public Model_ExtendedUserDO selectModelByModelId(Long id, Long userSelfId) {
        // 获取模型
        LambdaQueryWrapper<Model3dDO> model3dWrapper = new LambdaQueryWrapper<>();
        model3dWrapper.eq(Model3dDO::getId, id);
        model3dWrapper.ge(Model3dDO::getVisible, VisibleEnum.PUBLIC);
        model3dWrapper.select(Model3dDO::getId, Model3dDO::getUserId, Model3dDO::getTitle, Model3dDO::getCover,
                Model3dDO::getEntity, Model3dDO::getStyle, Model3dDO::getType, Model3dDO::getCreateTime);

        Model3dDO model3dDO = model3dMapper.selectOne(model3dWrapper);

        // 获取用户信息
        Long userId = model3dDO.getUserId();
        LambdaQueryWrapper<UserDO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(UserDO::getId, userId);
        userWrapper.select(UserDO::getId, UserDO::getAvatar, UserDO::getName);
        UserDO userDO =userMapper.selectOne(userWrapper);

        // 判断是否已关注
        LambdaQueryWrapper<RelationshipsDO> relationshipsDOWrapper = new LambdaQueryWrapper<>();
        relationshipsDOWrapper.eq(RelationshipsDO::getTgtUserId,userId)
                .and(w->w.eq(RelationshipsDO::getSrcUserId,userSelfId))
                .and(w->w.eq(RelationshipsDO::getIsFollowing,true));

        Boolean isFollowed = !Objects.isNull(relationshipsMapperMapper.selectOne(relationshipsDOWrapper));

        // 构建扩展用户信息
        ExtendedUserDO extendedUserDO = ExtendedUserDO.from(userDO, isFollowed);

        return Model_ExtendedUserDO.from(extendedUserDO, model3dDO);
    }
}
