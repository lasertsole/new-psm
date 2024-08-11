package com.psm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Auth.LoginUser;
import com.psm.domain.User.UserDAO;
import com.psm.domain.UtilsDom.ResponseDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDAO> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseDTO login(UserDAO user) {
        try {
            //AuthenticationManager authenticate进行认证
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword());
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
            return new ResponseDTO<>(HttpStatus.OK,"登出成功");
        }
        catch (RuntimeException e){
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST,"登出失败");
        }
        catch (Exception e){
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR,"服务器错误"+e.getMessage());
        }
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
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

    /**
     * 注册
     *
     * @return
     */
    @Override
    public ResponseDTO deleteUser() {
        try {
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //根据用户id删除redis中的用户信息
            redisCache.deleteObject("login:"+id);

            //根据用户id删除pg中的用户信息
            userMapper.deleteById(id);

            return new ResponseDTO<>(HttpStatus.OK,"注销成功");
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
     * 更新
     *
     * @param user
     * @return
     */
    public ResponseDTO updateUser(UserDAO user) {
        try {
            //TODO
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //更新用户信息
            LambdaUpdateWrapper<UserDAO> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(UserDAO::getId,id);

            wrapper.set(ObjectUtil.isEmpty(user.getName()), UserDAO::getName, user.getName());
            wrapper.set(ObjectUtil.isEmpty(user.getPassword()), UserDAO::getPassword, user.getPassword());
            wrapper.set(ObjectUtil.isEmpty(user.getAvatar()), UserDAO::getAvatar, user.getAvatar());
            wrapper.set(ObjectUtil.isEmpty(user.getProfile()), UserDAO::getProfile, user.getProfile());
            wrapper.set(ObjectUtil.isEmpty(user.getPhone()), UserDAO::getPhone, user.getPhone());
            wrapper.set(ObjectUtil.isEmpty(user.getEmail()), UserDAO::getEmail, user.getEmail());
            wrapper.set(ObjectUtil.isEmpty(user.getSex()), UserDAO::getSex, user.getSex());

            userMapper.update(null,wrapper);

            return new ResponseDTO<>(HttpStatus.OK,"修改成功");
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
}