package com.psm.domain.User.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.infrastructure.annotation.spring.Repository;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.repository.UserDB;
import com.psm.domain.User.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Repository
public class UserDBImpl extends ServiceImpl<UserMapper, UserDAO> implements UserDB {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(UserDAO userDAO){
        userMapper.insert(userDAO);
    }

    @Override
    public UserDAO selectById(UserDAO userDAO){
        return userMapper.selectById(userDAO.getId());
    }

    @Override
    public void updateAvatar(UserDAO userDAO){
        LambdaUpdateWrapper<UserDAO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDAO::getId,userDAO.getId());
        wrapper.set(UserDAO::getAvatar, userDAO.getAvatar());
        userMapper.update(null,wrapper);
    }

    @Override
    public void updateInfo(UserDAO userDAO){
        LambdaUpdateWrapper<UserDAO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDAO::getId,userDAO.getId());

        wrapper.set(!ObjectUtil.isEmpty(userDAO.getName()), UserDAO::getName, userDAO.getName());
        wrapper.set(!ObjectUtil.isEmpty(userDAO.getProfile()), UserDAO::getProfile, userDAO.getProfile());
        wrapper.set(!ObjectUtil.isEmpty(userDAO.getPhone()), UserDAO::getPhone, userDAO.getPhone());
        wrapper.set(!ObjectUtil.isEmpty(userDAO.getEmail()), UserDAO::getEmail, userDAO.getEmail());
        wrapper.set(!ObjectUtil.isEmpty(userDAO.getSex()), UserDAO::getSex, userDAO.getSex());

        userMapper.update(null,wrapper);
    }

    @Override
    public String findPasswordById(UserDAO userDAO){
        LambdaQueryWrapper<UserDAO> queryWrapper = new LambdaQueryWrapper<UserDAO>();
        queryWrapper.select(UserDAO::getPassword)
                .eq(UserDAO::getId, userDAO.getId());

        List<Map<String, Object>> passwordList = userMapper.selectMaps(queryWrapper);
        return (String) passwordList.get(0).get("password");
    }

    @Override
    public void updatePasswordById(UserDAO userDAO){
        LambdaUpdateWrapper<UserDAO> uploadWrapper = new LambdaUpdateWrapper<UserDAO>();
        uploadWrapper.eq(UserDAO::getId, userDAO.getId())
                .set(UserDAO::getPassword, userDAO.getPassword());
        userMapper.update(null, uploadWrapper);
    }

    @Override
    public UserDAO findUserByName(UserDAO userDAO){
        LambdaQueryWrapper<UserDAO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDAO::getName, userDAO.getName());
        return userMapper.selectOne(queryWrapper);
    };

    @Override
    public List<UserDAO> findUsersByName(UserDAO userDAO){
        LambdaQueryWrapper<UserDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserDAO::getId, UserDAO::getName, UserDAO::getAvatar, UserDAO::getSex, UserDAO::getProfile,
                UserDAO::getCreateTime).like(UserDAO::getName, userDAO.getName());
        return userMapper.selectList(wrapper);
    }

    @Override
    public List<UserDAO> getUserOrderByCreateTimeAsc (Page<UserDAO> page){
        LambdaQueryWrapper<UserDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserDAO::getId, UserDAO::getName, UserDAO::getAvatar, UserDAO::getSex, UserDAO::getProfile,
                UserDAO::getCreateTime).orderByAsc(UserDAO::getCreateTime);

        //执行查询
        Page<UserDAO> resultPage = userMapper.selectPage(page, wrapper);

        return resultPage.getRecords();
    }
}
