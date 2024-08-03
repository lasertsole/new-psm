package com.psm.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.LoginUser;
import com.psm.domain.ResponseResult;
import com.psm.domain.User;
import com.psm.mapper.UserMapper;
import com.psm.service.UserService;
import com.psm.utils.JWTUtil;
import com.psm.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * SpringSecurity会自动调用这个方法进行用户名密码校验
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> quryWrapper = new LambdaQueryWrapper<>();
        quryWrapper.eq(User::getName, username);
        User user =  userMapper.selectOne(quryWrapper);

        // 如果没有查询到用户抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }

        //TODO 查询用户对应权限


        //把用户信息封装成UserDetails对象返回
        return new LoginUser(user);
    }

    public boolean register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.save(user);
    }
    public ResponseResult login(User user) {
        //AuthenticationManager authenticate进行认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //如果认证没通过，给出对应提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }

        //如果认证通过了，使用id生成jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        User loginUserInfo = loginUser.getUser();
        String id = loginUserInfo.getId().toString();
        String jwt = JWTUtil.createJWT(id);

        //把完整信息存入redis，id作为key
        Map<String, Object> map = new HashMap<>();
        map.put("token",jwt);
        redisCache.setCacheObject("login:"+id,loginUser,1, TimeUnit.DAYS);

        return new ResponseResult(200, "登录成功", map);
    }
}
