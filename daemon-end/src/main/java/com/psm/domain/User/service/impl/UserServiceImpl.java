package com.psm.domain.User.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.entity.LoginUser.LoginUser;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.infrastructure.Convertor.UserConvertor;
import com.psm.domain.User.repository.LoginUserRedis;
import com.psm.domain.User.repository.UserOSS;
import com.psm.domain.User.repository.UserRepository;
import com.psm.domain.User.service.UserService;
import com.psm.domain.User.infrastructure.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOSS userOSS;

    @Autowired
    private LoginUserRedis loginUserRedis;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${spring.security.jwt.expiration}")
    private Long expiration;//jwt有效期

    @Autowired
    private UserConvertor userConvertor;

    @Override
    public UserDAO getAuthorizedUser(){
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        return loginUser.getUser();
    }

    @Override
    public Long getAuthorizedUserId() {
        return getAuthorizedUser().getId();
    }

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
            loginUserRedis.addLoginUser(id, loginUser);

            Map<String, Object> map = new HashMap<>();
            map.put("token",jwt);
            map.put("user",loginUserInfo);
            return map;
        }catch (Exception e) {
            throw new RuntimeException("Server error when login: "+e.getMessage());
        }
    }

    @Override
    public void logout() {
        try {
            //获取SecurityContextHolder中的用户id
            Long id = getAuthorizedUserId();

            //根据用户id删除redis中的用户信息
            loginUserRedis.removeLoginUser(String.valueOf(id));
        }
        catch (Exception e){
            throw new RuntimeException("Server error when logout: "+e.getMessage());
        }
    }

    @Override
    public Map<String, Object> register(UserDTO userDTO) throws DuplicateKeyException{
        try{
            //将前端传来的user对象拷贝到register对象中,并加密register对象的密码
            UserDAO register = userConvertor.DTO2DAO(userDTO);
            register.setPassword(passwordEncoder.encode(register.getPassword()));

            //将register对象保存到数据库
            userRepository.save(register);

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
            loginUserRedis.removeLoginUser(String.valueOf(id));

            //根据用户id删除pg中的用户信息
            userRepository.removeById(id);

        } catch (Exception e) {
            throw new RuntimeException("Server error when deleteUser: "+e.getMessage());
        }
    }

    @Override
    public String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile){
        try{
            //更新oss中用户头像信息
            String avatarUrl = userOSS.updateAvatar(oldAvatarUrl, newAvatarFile);

            //更新数据库中用户头像信息
            Long id = getAuthorizedUserId();//获取SecurityContextHolder中的用户id
            UserDAO userDAO = new UserDAO();
            userDAO.setId(id);
            userDAO.setAvatar(avatarUrl);
            userRepository.updateAvatar(userDAO);

            //更新redis中用户头像信息
            LoginUser loginUser = new LoginUser(getUserByID(id));
            loginUserRedis.addLoginUser(String.valueOf(id), loginUser);

            return avatarUrl;
        }
        catch (Exception e){//上传失败
            throw new RuntimeException("avatarUrl upload failed");
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        try {
            //获取SecurityContextHolder中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Long id = loginUser.getUser().getId();

            //将UserDTO转化为UserDAO
            UserDAO userDAO = userConvertor.DTO2DAO(userDTO);
            userDAO.setId(id);

            //更新用户信息
            userRepository.updateUser(userDAO);

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

            //获取数据库中用户的password
            UserDAO userDAO = new UserDAO();
            userDAO.setId(id);
            String passwordFromDB = userRepository.findPasswordById(userDAO);

            //判断旧密码是否正确
            if(passwordEncoder.matches(password,passwordFromDB)){
                throw new RuntimeException("Password error");
            }

            //判断新密码是否与数据库中用户的password相同
            if(passwordEncoder.matches(changePassword,passwordFromDB)){
                throw new RuntimeException("New password cannot be the same as the old password");
            }

            //将新密码覆盖数据库中的password
            userDAO.setId(id);
            userDAO.setPassword(passwordEncoder.encode(changePassword));
            userRepository.updatePasswordById(userDAO);

        } catch (Exception e) {
            throw new RuntimeException("Server error when updatePassword: "+e.getMessage());
        }
    }

    @Override
    public UserDAO getUserByID(Long id) {
        try {
            //获取用户信息
            UserDAO userDAO = userRepository.getById(id);

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
            UserDAO userDAO = new UserDAO();
            userDAO.setName(name);
            List<UserDAO> userDAOs = userRepository.findUsersByName(userDAO);

            //判断用户是否存在
            if(!userDAOs.isEmpty()){
                return userDAOs;
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
        //分页
        Page<UserDAO> page = new Page<>(currentPage,pageSize);

        //返回结果
        return userRepository.getUserOrderByCreateTimeAsc(page);
    }
}