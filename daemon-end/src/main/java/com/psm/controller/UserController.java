package com.psm.controller;

import com.psm.domain.User.UserDAO;
import com.psm.domain.User.UserDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")//登录
    public ResponseDTO login(@Valid @RequestBody UserDTO userDto){
        UserDAO user = new UserDAO();
        BeanUtils.copyProperties(userDto, user);
        return userService.login(user);
    }

    @PostMapping("/register")//注册
    public ResponseDTO register(@Valid @RequestBody UserDTO userDto){
        //注册
        UserDAO user = new UserDAO();
        BeanUtils.copyProperties(userDto, user);
        return userService.register(user);
    }

    @DeleteMapping("/logout")//登出
    public ResponseDTO logout()
    {
        return userService.logout();
    }

    @DeleteMapping("/deleteUser")//销号
    public ResponseDTO deleteUser()
    {
        return userService.deleteUser();
    }

    @PutMapping("/updateUser")//更新用户信息(除了密码)
    public ResponseDTO updateUser(@Valid @RequestBody UserDTO userDto)
    {
        UserDAO user = new UserDAO();
        BeanUtils.copyProperties(userDto, user);
        return userService.updateUser(user);
    }

    @PutMapping("/updatePassword")//更新密码
    public ResponseDTO updatePassword(@Valid @RequestBody UserDTO userDto)
    {
        String password = userDto.getPassword();
        String changePassword = userDto.getChangePassword();
        return userService.updatePassword(password,changePassword);
    }
}
