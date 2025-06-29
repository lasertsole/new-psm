package com.psm.domain.Independent.User.Single.user.service.impl;

import com.psm.domain.Independent.User.Single.user.pojo.entity.LoginUser.LoginUser;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserBO;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserDO;
import com.psm.infrastructure.RepositoryImpl.User.user.LoginUserCache;
import com.psm.infrastructure.RepositoryImpl.User.user.UserDB;
import com.psm.domain.Independent.User.Single.user.service.AuthUserService;
import com.psm.domain.Independent.User.Single.user.event.bus.security.utils.JWT.JWTUtil;
import com.psm.infrastructure.Cache.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
public class UserDetailsServiceImpl implements AuthUserService {
    @Autowired
    private UserDB userDB;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private LoginUserCache loginUserCache;

    /**
     * SpringSecurity会自动调用这个方法进行用户名密码校验
     *
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException 用户不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        UserDO user = new UserDO();
        user.setName(username);

        UserDO user1 = userDB.findUserByName(user);

        // 如果没有查询到用户抛出异常
        if (Objects.isNull(user1)){
            throw new RuntimeException("Incorrect username or password");
        }

        //TODO 查询用户对应权限

        //把用户信息封装成UserDetails对象返回
        return new LoginUser(user1);
    }

    @Override
    public UserBO authUserToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("Invalid token");
        }

        //解析token
        String userid;
        try {
            Claims claims = jwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }

        //从redis中获取用户信息
        LoginUser loginUser = loginUserCache.getLoginUser(userid);

        if(Objects.isNull(loginUser)){
            throw new RuntimeException("User not logged in");
        }

        return loginUser.getUserDO().toBO();
    }
}
