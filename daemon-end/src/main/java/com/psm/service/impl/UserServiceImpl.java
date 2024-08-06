package com.psm.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Auth.LoginUser;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.domain.User.UserDAO;
import com.psm.mapper.UserMapper;
import com.psm.service.UserService;
import com.psm.utils.JWTUtil;
import com.psm.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDAO> implements UserService, UserDetailsService {

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
        LambdaQueryWrapper<UserDAO> quryWrapper = new LambdaQueryWrapper<>();
        quryWrapper.eq(UserDAO::getName, username);
        UserDAO user = userMapper.selectOne(quryWrapper);

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
    public ResponseDTO login(UserDAO user) {
        try {
            //AuthenticationManager authenticate进行认证
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            //如果认证通过了，使用id生成jwt
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            UserDAO loginUserInfo = loginUser.getUser();
            String id = loginUserInfo.getId().toString();
            String jwt = JWTUtil.createJWT(id);

            //把完整信息存入redis，id作为key
            redisCache.setCacheObject("login:"+id,loginUser,1, TimeUnit.DAYS);

            Map<String, Object> map = new HashMap<>();
            map.put("token",jwt);
            return new ResponseDTO(HttpStatus.OK,"登录成功",map);
        } catch (LockedException e){
            return new ResponseDTO(HttpStatus.TOO_MANY_REQUESTS,"账号被锁定");
        } catch (BadCredentialsException e) {
            return new ResponseDTO(HttpStatus.UNAUTHORIZED,"认证失败");
        } catch (DisabledException e){
            return new ResponseDTO(HttpStatus.FORBIDDEN,"账号被禁用");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"服务器错误: "+e.getMessage());
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

    public ResponseDTO register(UserDAO user) {
        try{
            //将前端传来的user对象拷贝到register对象中,并加密register对象的密码
            UserDAO register = new UserDAO();
            BeanUtils.copyProperties(user, register);
            register.setPassword(passwordEncoder.encode(register.getPassword()));

            //将register对象保存到数据库
            save(register);

            //使用未加密密码的user对象登录
            ResponseDTO<Map<String, Object>> loginResponseDTO = login(user);

            if (loginResponseDTO.getCode() == HttpStatus.OK.value()){
                return new ResponseDTO<>(HttpStatus.OK,"注册成功",loginResponseDTO.getData());
            }
            else return new ResponseDTO<>(HttpStatus.OK,loginResponseDTO.getMsg());

        }
        catch (DuplicateKeyException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"注册失败，账户已存在");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"服务器错误:"+e.getMessage());
        }
    }
}