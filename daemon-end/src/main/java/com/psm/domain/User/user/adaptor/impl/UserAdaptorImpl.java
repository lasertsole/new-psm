package com.psm.domain.User.user.adaptor.impl;

import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.annotation.spring.Adaptor;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.domain.User.user.infrastructure.convertor.UserConvertor;
import com.psm.domain.User.user.service.UserService;
import com.psm.infrastructure.utils.MybatisPlus.Page.PageDTO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Adaptor
public class UserAdaptorImpl implements UserAdaptor {
    @Autowired
    UserService userService;

    @Override
    public UserBO getAuthorizedUser() {
        return UserConvertor.INSTANCE.DAO2BO(userService.getAuthorizedUser());
    }

    @Override
    public Long getAuthorizedUserId() {
        return userService.getAuthorizedUserId();
    }

    @Override
    public Map<String, Object> login(@Valid UserDTO userDTO) throws LockedException, BadCredentialsException, DisabledException, InvalidParameterException{
        // 参数判空
        if(
                StringUtils.isBlank(userDTO.getName())
                ||StringUtils.isBlank(userDTO.getPassword())
        ){
            throw new InvalidParameterException("Invalid parameter");
        }

        // 登录
        Map<String, Object> map = userService.login(userDTO);

        // 获取用户
        UserDAO userDAO = (UserDAO) map.get("user");

        // 判断用户是否存在
        if(userDAO == null){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDAO转换为UserBO
        UserBO userBO = UserConvertor.INSTANCE.DAO2BO(userDAO);
        map.put("user", userBO);

        return map;
    }

    @Override
    public void logout() {
        userService.logout();
    }

    @Override
    public Map<String, Object> register(@Valid UserDTO userDTO) throws DuplicateKeyException, InvalidParameterException {
        // 参数判空
        if(
                StringUtils.isBlank(userDTO.getName())
                ||StringUtils.isBlank(userDTO.getPassword())
                ||StringUtils.isBlank(userDTO.getEmail())
        ){
            throw new InvalidParameterException("Invalid parameter");
        }

        // 注册
        Map<String, Object> map = userService.register(userDTO);

        // 获取用户
        UserDAO userDAO = (UserDAO) map.get("user");

        // 判断用户是否存在
        if(userDAO == null){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDAO转换为UserBO
        UserBO userBO = UserConvertor.INSTANCE.DAO2BO(userDAO);
        map.put("user", userBO);

        return map;
    }

    @Override
    public void deleteUser() {
        userService.deleteUser();
    }

    @Override
    public String updateAvatar(@Valid UserDTO userDTO) throws InvalidParameterException, Exception{
        if (
                StringUtils.isBlank(userDTO.getOldAvatarUrl())
                &&Objects.isNull(userDTO.getAvatar())
        )
            throw new InvalidParameterException("Invalid parameter");

        return userService.updateAvatar(userDTO.getOldAvatarUrl(), userDTO.getAvatar());
    };

    @Override
    public void updateInfo(@Valid UserDTO userDTO) throws InvalidParameterException {
        // 参数判空
        if(
                StringUtils.isBlank(userDTO.getName())
                &&Objects.isNull(userDTO.getSex())
                &&StringUtils.isBlank(userDTO.getPhone())
                &&StringUtils.isBlank(userDTO.getEmail())
                &&StringUtils.isBlank(userDTO.getProfile())
        )
            throw new InvalidParameterException("Invalid parameter");


        // 修改用户
        userService.updateInfo(userDTO);
    }

    @Override
    public void updatePassword(@Valid UserDTO userDTO) throws InvalidParameterException {
        // 参数判空
        if(
                StringUtils.isBlank(userDTO.getPassword())
                ||StringUtils.isBlank(userDTO.getChangePassword())
        )
        {
            throw new InvalidParameterException("Invalid parameter");
        }

        // 修改密码
        userService.updatePassword(userDTO.getPassword(), userDTO.getChangePassword());
    }

    @Override
    public UserBO getUserById(@Valid UserDTO userDTO) throws InvalidParameterException {
        // 参数判空
        if(Objects.isNull(userDTO.getId())){
            throw new InvalidParameterException("Invalid parameter");
        }

        // 获取用户
        Long id = userDTO.getId();
        UserDAO userDAO = userService.getUserByID(id);

        // 判断用户是否存在
        if(Objects.isNull(userDAO)){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDAO转换为UserBO
        return UserConvertor.INSTANCE.DAO2BO(userDAO);
    }

    @Override
    public UserBO getUserById(Long id) throws InvalidParameterException {
        UserDAO userDAO = userService.getUserByID(id);

        // 判断用户是否存在
        if(Objects.isNull(userDAO)){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDAO转换为UserBO
        return UserConvertor.INSTANCE.DAO2BO(userDAO);
    }

    @Override
    public List<UserBO> getUserByName(@Valid UserDTO userDTO) throws InvalidParameterException {
        // 参数判空
        if(StringUtils.isBlank(userDTO.getName())){
            throw new InvalidParameterException("Invalid parameter");
        }
        String name = userDTO.getName();
        List<UserDAO> userDAOList = userService.getUserByName(name);

        // 判断用户是否存在
        if(Objects.isNull(userDAOList)){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDAO转换为UserBO
        return userDAOList.stream().map(UserConvertor.INSTANCE::DAO2BO).toList();
    }

    @Override
    public List<UserBO> getUserByName(String name) throws InvalidParameterException {
        List<UserDAO> userDAOList = userService.getUserByName(name);

        // 判断用户是否存在
        if(Objects.isNull(userDAOList)){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDAO转换为UserBO
        return userDAOList.stream().map(UserConvertor.INSTANCE::DAO2BO).toList();
    }

    @Override
    public List<UserBO> getUserOrderByCreateTimeAsc(@Valid PageDTO pageDTO){
        List<UserDAO> userDAOList = userService.getUserOrderByCreateTimeAsc(pageDTO.getCurrent(),
                pageDTO.getPage());

        // 判断用户列表是否存在
        if(userDAOList == null){
            throw new RuntimeException("The Subtitles does not exist.");
        }

        // 将DAO转换为BO
        return userDAOList.stream().map(
                UserConvertor.INSTANCE::DAO2BO
        ).toList();
    }

    @Override
    public List<UserBO> getUserByIds(List<Long> ids) {
        List<UserDAO> userDAOList = userService.getUserByIds(ids);
        // 将DAO转换为BO
        return userDAOList.stream().map(
                UserConvertor.INSTANCE::DAO2BO
        ).toList();
    }
}
