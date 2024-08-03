package com.psm.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.LoginUser;
import com.psm.domain.DTO.ResponseDTO;
import com.psm.domain.User;
import com.psm.mapper.UserMapper;
import com.psm.service.UserService;
import com.psm.utils.JWTUtil;
import com.psm.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> quryWrapper = new LambdaQueryWrapper<>();
        quryWrapper.eq(User::getName, username);
        User user = userMapper.selectOne(quryWrapper);

        // 如果没有查询到用户抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }

        //TODO 查询用户对应权限

        //把用户信息封装成UserDetails对象返回
        return new LoginUser(user);
    }

    @Override
    /**
     * 登录
     *
     * @param username
     * @return ResponseResult
     */
    public ResponseDTO login(User user) {
        //AuthenticationManager authenticate进行认证
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            //如果认证通过了，使用id生成jwt
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            User loginUserInfo = loginUser.getUser();
            String id = loginUserInfo.getId().toString();
            String jwt = JWTUtil.createJWT(id);

            //把完整信息存入redis，id作为key
            Map<String, Object> map = new HashMap<>();
            map.put("token",jwt);
            redisCache.setCacheObject("login:"+id,loginUser,1, TimeUnit.DAYS);

            return new ResponseDTO(HttpStatus.OK,"登录成功",map);
        } catch (LockedException e){
            return new ResponseDTO(HttpStatus.TOO_MANY_REQUESTS,"账号被锁定");
        } catch (BadCredentialsException e) {
            return new ResponseDTO(HttpStatus.UNAUTHORIZED,"认证失败");
        } catch (DisabledException e){
            return new ResponseDTO(HttpStatus.FORBIDDEN,"账号被禁用");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"服务器错误"+e.getMessage());
        }
    }

    /**
     * 退出登录
     *
     * @return ResponseResult
     */
    @Override
    public ResponseDTO logout() {
        try {
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //根据用户id删除redis中的用户信息
            redisCache.deleteObject("login:"+id);
            return new ResponseDTO<>(HttpStatus.OK,"注销成功");
        }
        catch (RuntimeException e){
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST,"注销失败");
        }
        catch (Exception e){
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR,"服务器错误"+e.getMessage());
        }
    }

    public ResponseDTO register(User user) {
        return null;
    }
}
