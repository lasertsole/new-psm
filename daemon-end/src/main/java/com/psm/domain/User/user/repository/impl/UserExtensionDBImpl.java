package com.psm.domain.User.user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.user.repository.mapper.UserExtensionMapper;
import com.psm.domain.User.user.repository.UserExtensionDB;
import com.psm.infrastructure.annotation.spring.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class UserExtensionDBImpl extends ServiceImpl<UserExtensionMapper, UserExtensionDAO> implements UserExtensionDB {
    @Autowired
    private UserExtensionMapper userExtensionMapper;

    @Override
    public void insert(UserExtensionDAO userExtension) { userExtensionMapper.insert(userExtension); }

    @Override
    public UserExtensionDAO selectById(UserExtensionDAO userExtension) { return userExtensionMapper.selectById(userExtension.getId()); }

    @Override
    public boolean updateById(UserExtensionDAO userExtension) {
        userExtensionMapper.updateById(userExtension);
        return true;
    }

    @Override
    public Page<UserExtensionDAO> getHasPublicModelOrderByCreateTimeDesc(Integer currentPage, Integer pageSize) {
        // 按照页配置获取发过模型的用户的ID列表,并按时间降序排序
        LambdaQueryWrapper<UserExtensionDAO> userExtensionWrapper = new LambdaQueryWrapper<>();
        userExtensionWrapper.select(UserExtensionDAO::getId);
        userExtensionWrapper.gt(UserExtensionDAO::getPublicModelNum, 0);
        userExtensionWrapper.orderByDesc(UserExtensionDAO::getCreateTime);
        Page<UserExtensionDAO> page = new Page<>(currentPage, pageSize);
        return userExtensionMapper.selectPage(page, userExtensionWrapper);
    };
}
