package com.psm.infrastructure.RepositoryImpl.User.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;
import com.psm.app.annotation.spring.Repository;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import com.psm.infrastructure.RepositoryImpl.User.user.UserDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@CacheConfig(cacheNames = "userCache")
public class UserDBImpl extends BaseDBRepositoryImpl<UserMapper, UserDO> implements UserDB {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(UserDO userDO){
        userMapper.insert(userDO);
    }

    @Override
    @Cacheable(key = "#id")
    public UserDO selectById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void updateAvatar(UserDO userDO){
        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId,userDO.getId());
        wrapper.set(UserDO::getAvatar, userDO.getAvatar());
        userMapper.update(null,wrapper);
    }

    @Override
    public Boolean updateInfo(UserDO userDO){
        return updateById(userDO);
    }

    @Override
    public String findPasswordById(UserDO userDO){
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO>();
        queryWrapper.select(UserDO::getPassword)
                .eq(UserDO::getId, userDO.getId());

        List<Map<String, Object>> passwordList = userMapper.selectMaps(queryWrapper);
        return (String) passwordList.get(0).get("password");
    }

    @Override
    public void updatePasswordById(UserDO userDO){
        LambdaUpdateWrapper<UserDO> uploadWrapper = new LambdaUpdateWrapper<UserDO>();
        uploadWrapper.eq(UserDO::getId, userDO.getId())
                .set(UserDO::getPassword, userDO.getPassword());
        userMapper.update(null, uploadWrapper);
    }

    @Override
    public UserDO findUserByName(UserDO userDO){
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getName, userDO.getName());
        return userMapper.selectOne(queryWrapper);
    };

    @Override
    public List<UserDO> findUsersByName(UserDO userDO){
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserDO::getId, UserDO::getName, UserDO::getAvatar, UserDO::getSex, UserDO::getProfile,
                UserDO::getCreateTime).like(UserDO::getName, userDO.getName());
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
