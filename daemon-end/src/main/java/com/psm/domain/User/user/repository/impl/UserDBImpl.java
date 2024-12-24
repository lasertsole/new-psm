package com.psm.domain.User.user.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.repository.UserDB;
import com.psm.app.annotation.spring.Repository;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Repository
public class UserDBImpl extends BaseDBRepositoryImpl<UserMapper, UserDO> implements UserDB {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(UserDO userDAO){
        userMapper.insert(userDAO);
    }

    @Override
    public UserDO selectById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public UserDO selectById(UserDO userDAO){
        return userMapper.selectById(userDAO.getId());
    }

    @Override
    public void updateAvatar(UserDO userDAO){
        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId,userDAO.getId());
        wrapper.set(UserDO::getAvatar, userDAO.getAvatar());
        userMapper.update(null,wrapper);
    }

    @Override
    public void updateInfo(UserDO userDAO){
        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId,userDAO.getId());

        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getName()), UserDO::getName, userDAO.getName());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getProfile()), UserDO::getProfile, userDAO.getProfile());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getPhone()), UserDO::getPhone, userDAO.getPhone());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getEmail()), UserDO::getEmail, userDAO.getEmail());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getSex()), UserDO::getSex, userDAO.getSex());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getPublicModelNum()), UserDO::getPublicModelNum, userDAO.getPublicModelNum());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getModelMaxStorage()), UserDO::getModelMaxStorage, userDAO.getModelMaxStorage());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getModelCurStorage()), UserDO::getModelCurStorage, userDAO.getModelCurStorage());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getIsIdle()), UserDO::getIsIdle, userDAO.getIsIdle());
        wrapper.set(ObjectUtil.isNotEmpty(userDAO.getCanUrgent()), UserDO::getCanUrgent, userDAO.getCanUrgent());

        userMapper.update(null,wrapper);
    }

    @Override
    public String findPasswordById(UserDO userDAO){
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO>();
        queryWrapper.select(UserDO::getPassword)
                .eq(UserDO::getId, userDAO.getId());

        List<Map<String, Object>> passwordList = userMapper.selectMaps(queryWrapper);
        return (String) passwordList.get(0).get("password");
    }

    @Override
    public void updatePasswordById(UserDO userDAO){
        LambdaUpdateWrapper<UserDO> uploadWrapper = new LambdaUpdateWrapper<UserDO>();
        uploadWrapper.eq(UserDO::getId, userDAO.getId())
                .set(UserDO::getPassword, userDAO.getPassword());
        userMapper.update(null, uploadWrapper);
    }

    @Override
    public UserDO findUserByName(UserDO userDAO){
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getName, userDAO.getName());
        return userMapper.selectOne(queryWrapper);
    };

    @Override
    public List<UserDO> findUsersByName(UserDO userDAO){
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserDO::getId, UserDO::getName, UserDO::getAvatar, UserDO::getSex, UserDO::getProfile,
                UserDO::getCreateTime).like(UserDO::getName, userDAO.getName());
        return userMapper.selectList(wrapper);
    }

    @Override
    public List<UserDO> selectUserOrderByCreateTimeAsc (Page<UserDO> page) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserDO::getId, UserDO::getName, UserDO::getAvatar, UserDO::getSex, UserDO::getProfile,
                UserDO::getCreateTime).orderByAsc(UserDO::getCreateTime);

        //执行查询
        Page<UserDO> resultPage = userMapper.selectPage(page, wrapper);

        return resultPage.getRecords();
    }

    @Override
    public List<UserDO> selectUserByIds(List<Long> ids) {
        // 按照用户ID列表获取用户列表
        LambdaQueryWrapper<UserDO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.in(UserDO::getId, ids);
        return userMapper.selectList(userWrapper);
    }
}
