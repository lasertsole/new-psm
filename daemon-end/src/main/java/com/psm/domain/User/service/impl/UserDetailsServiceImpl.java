package com.psm.domain.User.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.User.entity.LoginUser.LoginUser;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    /**
     * SpringSecurity会自动调用这个方法进行用户名密码校验
     *
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<UserDAO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDAO::getName, username);
        UserDAO user = userMapper.selectOne(queryWrapper);

        // 如果没有查询到用户抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("Incorrect username or password");
        }

        //TODO 查询用户对应权限

        //把用户信息封装成UserDetails对象返回
        return new LoginUser(user);
    }
}
