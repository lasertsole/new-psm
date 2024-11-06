package com.psm.domain.Model.modelExtendedUserBind.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.modelExtendedUserBind.repository.ModelExtendedUserBindDB;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindDAO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.valueObject.ExtendedUserDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.infrastructure.DB.FollowerMapper;
import com.psm.infrastructure.DB.ModelMapper;
import com.psm.infrastructure.DB.UserExtensionMapper;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Slf4j
@Repository
public class ModelExtendedUserBindDBImpl implements ModelExtendedUserBindDB {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserExtensionMapper userExtensionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FollowerMapper followerMapper;

    @Override
    public ModelExtendedUserBindDAO selectModelByModelId(Long id, Long userSelfId) {
        // 获取模型
        LambdaQueryWrapper<ModelDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.eq(ModelDAO::getId, id);
        modelWrapper.ge(ModelDAO::getVisible, VisibleEnum.PUBLIC);
        modelWrapper.select(ModelDAO::getId, ModelDAO::getUserId, ModelDAO::getTitle, ModelDAO::getCover,
                ModelDAO::getEntity, ModelDAO::getStyle, ModelDAO::getType, ModelDAO::getCreateTime);

        ModelDAO modelDAO = modelMapper.selectOne(modelWrapper);

        // 获取用户信息
        Long userId = modelDAO.getUserId();
        LambdaQueryWrapper<UserDAO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(UserDAO::getId, userId);
        userWrapper.select(UserDAO::getId, UserDAO::getAvatar, UserDAO::getName);
        UserDAO userDAO =userMapper.selectOne(userWrapper);

        // 判断是否已关注
        LambdaQueryWrapper<FollowerDAO> followerwrapper = new LambdaQueryWrapper<>();
        followerwrapper.eq(FollowerDAO::getTgtUserId,userId).and(
                w->w.eq(FollowerDAO::getSrcUserId,userSelfId));

        Boolean isFollowed = !Objects.isNull(followerMapper.selectOne(followerwrapper));

        // 构建扩展用户信息
        ExtendedUserDAO extendedUserDAO = ExtendedUserDAO.from(userDAO, isFollowed);

        return ModelExtendedUserBindDAO.from(extendedUserDAO, modelDAO);
    }
}
