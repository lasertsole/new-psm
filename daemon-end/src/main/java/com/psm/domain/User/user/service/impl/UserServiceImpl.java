package com.psm.domain.User.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.LoginUser.LoginUser;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import com.psm.domain.User.user.repository.LoginUserRedis;
import com.psm.domain.User.user.repository.UserExtensionDB;
import com.psm.domain.User.user.repository.UserOSS;
import com.psm.domain.User.user.repository.UserDB;
import com.psm.domain.User.user.service.UserService;
import com.psm.domain.User.user.infrastructure.utils.JWT.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDB userDB;

    @Autowired
    private UserOSS userOSS;

    @Autowired
    private LoginUserRedis loginUserRedis;

    @Autowired
    private UserExtensionDB userExtensionDB;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${spring.security.jwt.expiration}")
    private Long expiration;//jwt有效期

    @Override
    public UserDAO getAuthorizedUser(){
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        return loginUser.getUserDAO();
    }

    @Override
    public Long getAuthorizedUserId() {
        return getAuthorizedUser().getId();
    }

    @Override
    public Map<String, Object> login(UserDTO userDTO) throws LockedException,BadCredentialsException,DisabledException{
        // AuthenticationManager authenticate进行认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getName(),userDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 如果认证通过了，使用id生成jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        UserDAO loginUserInfo = loginUser.getUserDAO();
        String id = loginUserInfo.getId().toString();
        String jwt = jwtUtil.createJWT(id);

        // 把完整信息存入redis，id作为key(如果原先有则覆盖)
        loginUserRedis.addLoginUser(id, loginUser);

        Map<String, Object> map = new HashMap<>();
        map.put("token",jwt);
        map.put("user",loginUserInfo);

        return map;
    }

    @Override
    public void logout() {
        // 获取SecurityContextHolder中的用户id
        Long id = getAuthorizedUserId();

        // 根据用户id删除redis中的用户信息
        loginUserRedis.removeLoginUser(String.valueOf(id));
    }

    @Override
    @Transactional
    public Map<String, Object> register(UserDTO userDTO) throws DuplicateKeyException{
        // 将前端传来的user对象拷贝到register对象中,并加密register对象的密码
        UserDAO register = UserConvertor.INSTANCE.DTO2DAO(userDTO);
        register.setPassword(passwordEncoder.encode(register.getPassword()));

        // 将register对象保存到数据库
        userDB.save(register);

        // 在数据库中创建一个UserExtension记录
        userExtensionDB.insert(new UserExtensionDAO(register.getId()));

        // 使用未加密密码的user对象登录
        Map<String, Object> loginMap = login(userDTO);

        if (loginMap.isEmpty()){
            throw new RuntimeException("The user does not exist.");
        }

        return loginMap;
    }

    @Override
    public void deleteUser() {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUserDAO().getId();

        // 根据用户id删除redis中的用户信息
        loginUserRedis.removeLoginUser(String.valueOf(id));

        // 根据用户id删除pg中的用户信息
        userDB.removeById(id);
    }

    @Override
    public String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile) throws Exception {
        // 获取SecurityContextHolder中的用户id
        String userId = String.valueOf(getAuthorizedUserId());

        // 更新oss中用户头像信息
        String avatarUrl = userOSS.updateAvatar(oldAvatarUrl, newAvatarFile, userId);

        // 更新数据库中用户头像信息
        Long id = getAuthorizedUserId();//获取SecurityContextHolder中的用户id
        UserDAO userDAO = new UserDAO();
        userDAO.setId(id);
        userDAO.setAvatar(avatarUrl);
        userDB.updateAvatar(userDAO);

        // 更新redis中用户头像信息
        loginUserRedis.updateLoginUser(userDAO);

        return avatarUrl;
    }

    @Override
    public void updateInfo(UserDTO userDTO) {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUserDAO().getId();

        // 将UserDTO转化为UserDAO
        UserDAO userDAO = UserConvertor.INSTANCE.DTO2DAO(userDTO);
        userDAO.setId(id);

        // 更新用户信息
        userDB.updateInfo(userDAO);

        // 更新redis中用户信息
        loginUserRedis.updateLoginUser(userDAO);
    }

    @Override
    public void updatePassword(String password, String changePassword) {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUserDAO().getId();

        // 判断新密码是否与旧密码相同
        if(password.equals(changePassword)){
            throw new RuntimeException("New password cannot be the same as the old password");
        }

        // 获取数据库中用户的password
        UserDAO userDAO = new UserDAO();
        userDAO.setId(id);
        String passwordFromDB = userDB.findPasswordById(userDAO);

        // 判断旧密码是否正确
        if(!passwordEncoder.matches(password,passwordFromDB)){
            throw new RuntimeException("Password error");
        }

        // 将新密码加密
        String encodePassword = passwordEncoder.encode(changePassword);

        // 将新密码覆盖数据库中的password
        userDAO.setId(id);
        userDAO.setPassword(encodePassword);
        userDB.updatePasswordById(userDAO);

        // 更新redis中用户信息
        loginUserRedis.updateLoginUser(userDAO);
    }

    @Override
    public UserDAO getUserByID(Long id) {
        // 获取用户信息
        UserDAO userDAO = userDB.getById(id);

        // 判断用户是否存在
        if(userDAO != null){
            return userDAO;
        }
        else{// 用户不存在
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public List<UserDAO> getUserByName(String name) {
        // 获取用户信息
        UserDAO userDAO = new UserDAO();
        userDAO.setName(name);
        List<UserDAO> userDAOs = userDB.findUsersByName(userDAO);

        // 判断用户是否存在
        if(!userDAOs.isEmpty()){
            return userDAOs;
        }
        else{// 用户不存在
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public List<UserDAO> getUserOrderByCreateTimeAsc(Integer currentPage, Integer pageSize){
        // 分页
        Page<UserDAO> page = new Page<>(currentPage,pageSize);

        // 返回结果
        return userDB.selectUserOrderByCreateTimeAsc(page);
    }

    @Override
    public List<UserDAO> getUserByIds(List<Long> ids) {
        return userDB.selectUserByIds(ids);
    }
}