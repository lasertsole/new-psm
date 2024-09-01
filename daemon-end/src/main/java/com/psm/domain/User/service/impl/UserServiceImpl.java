package com.psm.domain.User.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.User.entity.LoginUser;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.repository.UserMapper;
import com.psm.domain.User.service.UserService;
import com.psm.utils.JWT.JWTUtil;
import com.psm.utils.OSS.UploadOSSUtil;
import com.psm.utils.Redis.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    JWTUtil jwtUtil;

    @Value("${spring.security.jwt.expiration}")
    public Long expiration;//jwt有效期

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    @Value("${aliyun.oss.path.users.avatarFolderPath}")
    String avatarFolderPath;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Map<String, Object> login(UserDTO userDTO) throws LockedException,BadCredentialsException,DisabledException{
        try {

            //AuthenticationManager authenticate进行认证
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDTO.getName(),userDTO.getPassword());
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
            map.put("user",loginUserInfo);
            return map;
        }catch (Exception e) {
            throw new RuntimeException("Server error when login: "+e.getMessage());
        }
    }

    public Long getAuthorizedUserId() {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser().getId();
    }

    @Override
    public void logout() {
        try {
            //获取SecurityContextHolder中的用户id
            Long id = getAuthorizedUserId();

            //根据用户id删除redis中的用户信息
            redisCache.deleteObject("login:"+id);
        }
        catch (Exception e){
            throw new RuntimeException("Server error when logout: "+e.getMessage());
        }
    }

    @Override
    public Map<String, Object> register(UserDTO userDTO) throws DuplicateKeyException{
        try{
            //将前端传来的user对象拷贝到register对象中,并加密register对象的密码
            UserDAO register = new UserDAO();
            BeanUtils.copyProperties(userDTO, register);
            register.setPassword(passwordEncoder.encode(register.getPassword()));

            //将register对象保存到数据库
            save(register);

            //使用未加密密码的user对象登录
            Map<String, Object> loginMap = login(userDTO);

            if (loginMap.isEmpty()){
                throw new RuntimeException("The user does not exist.");
            }

            return loginMap;
        }
        catch (Exception e){
            throw new RuntimeException("Server error when register: "+e.getMessage());
        }
    }

    @Override
    public void deleteUser() {
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
        } catch (Exception e) {
            throw new RuntimeException("Server error when deleteUser: "+e.getMessage());
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        try {
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //更新用户信息
            LambdaUpdateWrapper<UserDAO> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(UserDAO::getId,id);

            //修改用户信息（除了密码）
            wrapper.set(!ObjectUtil.isEmpty(userDTO.getName()), UserDAO::getName, userDTO.getName());
            wrapper.set(!ObjectUtil.isEmpty(userDTO.getProfile()), UserDAO::getProfile, userDTO.getProfile());
            wrapper.set(!ObjectUtil.isEmpty(userDTO.getPhone()), UserDAO::getPhone, userDTO.getPhone());
            wrapper.set(!ObjectUtil.isEmpty(userDTO.getEmail()), UserDAO::getEmail, userDTO.getEmail());
            wrapper.set(!ObjectUtil.isEmpty(userDTO.getSex()), UserDAO::getSex, userDTO.getSex());
            if (!Objects.isNull(userDTO.getAvatar())){//如果有avatar，则上传到OSS
                try{
                    String avatarUrl = uploadOSSUtil.multipartUpload(userDTO.getAvatar(),avatarFolderPath);
                    wrapper.set(!ObjectUtil.isEmpty(userDTO.getAvatar()), UserDAO::getAvatar, avatarUrl);
                }
                catch (Exception e){//上传失败
                    throw new RuntimeException("avatarUrl upload failed");
                }
            }

            userMapper.update(null,wrapper);
        } catch (Exception e) {
            throw new RuntimeException("Server error when updateUser: "+e.getMessage());
        }
    }

    @Override
    public void updatePassword(String password, String changePassword) {
        try{
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //判断新密码是否与旧密码相同
            if(password.equals(changePassword)){
                throw new RuntimeException("New password cannot be the same as the old password");
            }

            //获取数据库中用户的password和和用户提交的password是否相同
            LambdaQueryWrapper<UserDAO> queryWrapper = new LambdaQueryWrapper<UserDAO>();
            queryWrapper.select(UserDAO::getPassword)
                    .eq(UserDAO::getId, id);

            List<Map<String, Object>> passwordList = userMapper.selectMaps(queryWrapper);
            String passwordFromDB = (String) passwordList.get(0).get("password");

            //判断旧密码是否正确
            if(passwordEncoder.matches(password,passwordFromDB)){
                throw new RuntimeException("Password error");
            }

            //判断新密码是否与数据库中用户的password相同
            if(passwordEncoder.matches(changePassword,passwordFromDB)){
                throw new RuntimeException("New password cannot be the same as the old password");
            }

            //将新密码覆盖数据库中的password
            LambdaUpdateWrapper<UserDAO> uploadWrapper = new LambdaUpdateWrapper<UserDAO>();
            uploadWrapper.eq(UserDAO::getId, id)
                    .set(UserDAO::getPassword, passwordEncoder.encode(changePassword));
            userMapper.update(null, uploadWrapper);
        } catch (Exception e) {
            throw new RuntimeException("Server error when updatePassword: "+e.getMessage());
        }
    }

    @Override
    public UserDAO getUserByID(Long id) {
        try {
            //获取用户信息
            UserDAO userDAO = getById(id);

            //判断用户是否存在
            if(userDAO != null){
                return userDAO;
            }
            else{//用户不存在
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Server error when getUserByID: "+e.getMessage());
        }
    }

    @Override
    public List<UserDAO> getUserByName(String name) {
        try {
            //获取用户信息
            LambdaQueryWrapper<UserDAO> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(UserDAO::getId, UserDAO::getName, UserDAO::getAvatar, UserDAO::getSex, UserDAO::getProfile,
                    UserDAO::getCreateTime).like(UserDAO::getName, name);
            List<UserDAO> userDAOList = userMapper.selectList(wrapper);

            //判断用户是否存在
            if(!userDAOList.isEmpty()){
                return userDAOList;
            }
            else{//用户不存在
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Server error when getUserByName: "+e.getMessage());
        }
    }

    @Override
    public List<UserDAO> getUserOrderByCreateTimeAsc(Integer currentPage, Integer pageSize){
        //获取用户信息
        LambdaQueryWrapper<UserDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserDAO::getId, UserDAO::getName, UserDAO::getAvatar, UserDAO::getSex, UserDAO::getProfile,
                UserDAO::getCreateTime).orderByAsc(UserDAO::getCreateTime);

        //分页
        Page<UserDAO> page = new Page<>(currentPage,pageSize);

        //执行查询
        Page<UserDAO> resultPage = userMapper.selectPage(page, wrapper);

        //返回结果
        return resultPage.getRecords();
    }
}