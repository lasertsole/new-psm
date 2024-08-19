package com.psm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Auth.LoginUser;
import com.psm.domain.User.UserDAO;
import com.psm.domain.User.UserVO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.mapper.UserMapper;
import com.psm.service.UserService;
import com.psm.utils.JWT.JWTUtil;
import com.psm.utils.Redis.RedisCache;
import io.netty.util.internal.StringUtil;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Setter
@Service
@ConfigurationProperties(prefix = "jwt")//配置和jwt一样的过期时间
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDAO> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    JWTUtil jwtUtil;

    public Long expiration;//jwt有效期

    /**
     * 登录
     *
     * @param user
     * @return
     */
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
            String jwt = jwtUtil.createJWT(id);

            //把完整信息存入redis，id作为key(如果原先有则覆盖)
            redisCache.setCacheObject("login:"+id,loginUser,Math.toIntExact(expiration / 1000 / 3600), TimeUnit.HOURS);

            Map<String, Object> map = new HashMap<>();
            map.put("token",jwt);
            return new ResponseDTO(HttpStatus.OK,"Login successful",map);
        } catch (LockedException e){
            return new ResponseDTO(HttpStatus.TOO_MANY_REQUESTS,"Account is locked");
        } catch (BadCredentialsException e) {
            return new ResponseDTO(HttpStatus.UNAUTHORIZED,"Authentication failed");
        } catch (DisabledException e){
            return new ResponseDTO(HttpStatus.FORBIDDEN,"Account is disabled");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error: "+e.getMessage());
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
            return new ResponseDTO(HttpStatus.OK,"Logout successful");
        }
        catch (RuntimeException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"Logout failed");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error"+e.getMessage());
        }
    }

    /**
     * 注册
     *
     * @param userDAO
     * @return
     */
    public ResponseDTO register(UserDAO userDAO) {
        try{
            //将前端传来的user对象拷贝到register对象中,并加密register对象的密码
            UserDAO register = new UserDAO();
            BeanUtils.copyProperties(userDAO, register);
            register.setPassword(passwordEncoder.encode(register.getPassword()));

            //将register对象保存到数据库
            save(register);

            //使用未加密密码的user对象登录
            ResponseDTO loginResponseDTO = login(userDAO);

            if (loginResponseDTO.getCode() == HttpStatus.OK.value()){
                return new ResponseDTO(HttpStatus.OK,"Registration successful",loginResponseDTO.getData());
            }
            else return new ResponseDTO(loginResponseDTO.getCode(),loginResponseDTO.getMsg());

        }
        catch (DuplicateKeyException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"Registration failed, account already exists");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error:"+e.getMessage());
        }
    }

    /**
     * 删除用户
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

            return new ResponseDTO(HttpStatus.OK,"Logout successful");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error: "+e.getMessage());
        }
    }

    /**
     * 更新用户信息(除了密码)
     *
     * @param user
     * @return
     */
    public ResponseDTO updateUser(UserDAO user) {
        try {
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //更新用户信息
            LambdaUpdateWrapper<UserDAO> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(UserDAO::getId,id);

            //修改用户信息（除了密码）
            wrapper.set(!ObjectUtil.isEmpty(user.getName()), UserDAO::getName, user.getName());
            wrapper.set(!ObjectUtil.isEmpty(user.getAvatar()), UserDAO::getAvatar, user.getAvatar());
            wrapper.set(!ObjectUtil.isEmpty(user.getProfile()), UserDAO::getProfile, user.getProfile());
            wrapper.set(!ObjectUtil.isEmpty(user.getPhone()), UserDAO::getPhone, user.getPhone());
            wrapper.set(!ObjectUtil.isEmpty(user.getEmail()), UserDAO::getEmail, user.getEmail());
            wrapper.set(!ObjectUtil.isEmpty(user.getSex()), UserDAO::getSex, user.getSex());

            userMapper.update(null,wrapper);

            return new ResponseDTO(HttpStatus.OK,"Update successful");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error: "+e.getMessage());
        }
    }

    /**
     * 更新用户密码
     *
     * @param password
     * @param changePassword
     * @return
     */
    public ResponseDTO updatePassword(String password, String changePassword) {
        try{
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //判断密码是否为空
            if(StringUtil.isNullOrEmpty(password)){
                return new ResponseDTO(HttpStatus.BAD_REQUEST,"Password cannot be empty");
            }

            //判断新密码是否与旧密码相同
            if(password.equals(changePassword)){
                return new ResponseDTO(HttpStatus.BAD_REQUEST,"New password cannot be the same as the old password");
            }

            //获取数据库中用户的password和和用户提交的password是否相同
            LambdaQueryWrapper<UserDAO> queryWrapper = new LambdaQueryWrapper<UserDAO>();
            queryWrapper.select(UserDAO::getPassword)
                    .eq(UserDAO::getId, id);

            List<Map<String, Object>> passwordList = userMapper.selectMaps(queryWrapper);
            String passwordFromDB = (String) passwordList.get(0).get("password");

            //判断旧密码是否正确
            if(passwordEncoder.matches(password,passwordFromDB)){
                return new ResponseDTO(HttpStatus.BAD_REQUEST,"Password error");
            }

            //判断新密码是否与数据库中用户的password相同
            if(passwordEncoder.matches(changePassword,passwordFromDB)){
                return new ResponseDTO(HttpStatus.BAD_REQUEST,"New password cannot be the same as the old password");
            }

            //将新密码覆盖数据库中的password
            LambdaUpdateWrapper<UserDAO> uploadWrapper = new LambdaUpdateWrapper<UserDAO>();
            uploadWrapper.eq(UserDAO::getId, id)
                    .set(UserDAO::getPassword, passwordEncoder.encode(changePassword));
            userMapper.update(null, uploadWrapper);
            return new ResponseDTO(HttpStatus.OK,"Update successful");

        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error: "+e.getMessage());
        }
    }

    /**
     * 通过ID获取用户信息
     *
     * @param id
     * @return
     */
    public ResponseDTO getUserByID(Long id) {
        try {
            //获取用户信息
            UserDAO userDAO = getById(id);

            //判断用户是否存在
            if(userDAO != null){
                //将用户信息封装成UserVO
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userDAO, userVO);

                Map<String, Object> map = new HashMap<>();
                map.put("userInfo", userVO);
                return new ResponseDTO(HttpStatus.OK, "Get user information successfully", map);
            }

            //用户不存在
            return new ResponseDTO(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error: "+e.getMessage());
        }
    }

    /**
     * 通过用户名获取用户信息
     *
     * @param name
     * @return
     */
    public ResponseDTO getUserByName(String name) {
        try {
            //获取用户信息
            LambdaQueryWrapper<UserDAO> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(UserDAO::getId, UserDAO::getName, UserDAO::getAvatar, UserDAO::getSex, UserDAO::getProfile,
                    UserDAO::getCreateTime).like(UserDAO::getName, name);
            List<UserDAO> userDAOList = userMapper.selectList(wrapper);

            //判断用户是否存在
            if(!userDAOList.isEmpty()){
                //将用户信息封装成UserVO类型
                List<UserVO> userVOList = userDAOList.stream().map(
                        userDAO -> {
                            UserVO userVO = new UserVO();
                            BeanUtils.copyProperties(userDAO, userVO);
                            return userVO;
                        }
                ).collect(Collectors.toList());

                Map<String, Object> map = new HashMap<>();
                map.put("userInfo", userVOList);
                return new ResponseDTO(HttpStatus.OK, "Get user information successfully", map);
            }

            //用户不存在
            return new ResponseDTO(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error: "+e.getMessage());
        }
    }
}