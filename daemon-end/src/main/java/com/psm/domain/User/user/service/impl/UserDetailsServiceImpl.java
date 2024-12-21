package com.psm.domain.User.user.service.impl;

import com.psm.domain.User.user.entity.LoginUser.LoginUser;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.repository.UserDB;
import com.psm.domain.User.user.service.AuthUserService;
import com.psm.domain.User.user.event.bus.security.utils.JWT.JWTUtil;
import com.psm.infrastructure.Cache.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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

    private final Cache loginCache;

    public UserDetailsServiceImpl(CacheManager cacheManager) {
        loginCache = cacheManager.getCache("loginCache");
    }

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
        String cachekey = "login:" + userid;
        LoginUser loginUser = loginCache.get(cachekey, LoginUser.class);

        if(Objects.isNull(loginUser)){
            throw new RuntimeException("User not logged in");
        }

        return loginUser.getUserDO().toBO();
    }
}
