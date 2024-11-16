package com.psm.domain.Model.model_extendedUser.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.Model.model_extendedUser.repository.Model_ExtendedUserDB;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserDAO;
import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
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
    public Model_ExtendedUserDAO selectModelByModelId(Long id, Long userSelfId) {
        // 获取模型
        LambdaQueryWrapper<Model3dDAO> model3dWrapper = new LambdaQueryWrapper<>();
        model3dWrapper.eq(Model3dDAO::getId, id);
        model3dWrapper.ge(Model3dDAO::getVisible, VisibleEnum.PUBLIC);
        model3dWrapper.select(Model3dDAO::getId, Model3dDAO::getUserId, Model3dDAO::getTitle, Model3dDAO::getCover,
                Model3dDAO::getEntity, Model3dDAO::getStyle, Model3dDAO::getType, Model3dDAO::getCreateTime);

        Model3dDAO model3dDAO = model3dMapper.selectOne(model3dWrapper);

        // 获取用户信息
        Long userId = model3dDAO.getUserId();
        LambdaQueryWrapper<UserDAO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(UserDAO::getId, userId);
        userWrapper.select(UserDAO::getId, UserDAO::getAvatar, UserDAO::getName);
        UserDAO userDAO =userMapper.selectOne(userWrapper);

        // 判断是否已关注
        LambdaQueryWrapper<RelationshipsDAO> relationshipsDAOWrapper = new LambdaQueryWrapper<>();
        relationshipsDAOWrapper.eq(RelationshipsDAO::getTgtUserId,userId)
                .and(w->w.eq(RelationshipsDAO::getSrcUserId,userSelfId))
                .and(w->w.eq(RelationshipsDAO::getIsFollowing,true));

        Boolean isFollowed = !Objects.isNull(relationshipsMapperMapper.selectOne(relationshipsDAOWrapper));

        // 构建扩展用户信息
        ExtendedUserDAO extendedUserDAO = ExtendedUserDAO.from(userDAO, isFollowed);

        return Model_ExtendedUserDAO.from(extendedUserDAO, model3dDAO);
    }
}
