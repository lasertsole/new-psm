package com.psm.domain.User.adaptor.impl;

import com.psm.annotation.spring.Adaptor;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.entity.User.UserVO;
import com.psm.domain.User.infrastructure.Convertor.UserConvertor;
import com.psm.domain.User.service.UserService;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Adaptor
public class UserAdaptorImpl implements UserAdaptor {
    @Autowired
    UserService userService;

    @Autowired
    UserConvertor userConvertor;

    @Override
    public UserVO getAuthorizedUser() {
        return userConvertor.DAO2VO(userService.getAuthorizedUser());
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

        UserDAO userDAO = userConvertor.DTO2DAO(userDTO);

        // 登录
        Map<String, Object> map = userService.login(userDTO);

        // 获取用户
        userDAO = (UserDAO) map.get("user");

        // 判断用户是否存在
        if(userDAO == null){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDAO转换为UserVO
        UserVO userVO = userConvertor.DAO2VO(userDAO);
        map.put("user", userVO);

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

        // 将UserDAO转换为UserVO
        UserVO userVO = userConvertor.DAO2VO(userDAO);
        map.put("user", userVO);

        return map;
    }

    @Override
    public void deleteUser() {
        userService.deleteUser();
    }

    @Override
    public String updateAvatar(@Valid UserDTO userDTO) throws InvalidParameterException{
        if (
                StringUtils.isBlank(userDTO.getOldAvatarUrl())
                &&Objects.isNull(userDTO.getAvatar())
        )
            throw new InvalidParameterException("Invalid parameter");

        return userService.updateAvatar(userDTO.getOldAvatarUrl(), userDTO.getAvatar());
    };

    @Override
    public void updateUser(@Valid UserDTO userDTO) throws InvalidParameterException {
        // 参数判空
        if(
                StringUtils.isBlank(userDTO.getName())
                &&StringUtils.isBlank(userDTO.getPassword())
                &&StringUtils.isBlank(userDTO.getPhone())
                &&StringUtils.isBlank(userDTO.getEmail())
                &&StringUtils.isBlank(userDTO.getProfile())
        )
            throw new InvalidParameterException("Invalid parameter");


        // 修改用户
        userService.updateUser(userDTO);
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
    public UserVO getUserByID(@Valid UserDTO userDTO) throws InvalidParameterException {
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

        // 将UserDAO转换为UserVO
        return userConvertor.DAO2VO(userDAO);
    }

    @Override
    public List<UserVO> getUserByName(@Valid UserDTO userDTO) throws InvalidParameterException {
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

        // 将UserDAO转换为UserVO
        List<UserVO> userVOList = userDAOList.stream().map(userDAO -> {
            return userConvertor.DAO2VO(userDAO);
        }).toList();

        return userVOList;
    }

    @Override
    public List<UserVO> getUserOrderByCreateTimeAsc(@Valid PageDTO pageDTO){
        List<UserDAO> userDAOList = userService.getUserOrderByCreateTimeAsc(pageDTO.getCurrentPage(),
                pageDTO.getPageSize());

        // 判断用户列表是否存在
        if(userDAOList == null){
            throw new RuntimeException("The Subtitles does not exist.");
        }

        // 将DAO转换为VO
        List<UserVO> userVOList = userDAOList.stream().map(
                userDAO -> {
                    return userConvertor.DAO2VO(userDAO);
                }
        ).toList();

        return userVOList;
    };
}
