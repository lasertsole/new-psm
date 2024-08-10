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

    @GetMapping("/logout")//登出
    public ResponseDTO logout()
    {
        return userService.logout();
    }

    @PostMapping("/register")//注册
    public ResponseDTO register(@Valid @RequestBody UserDTO userDto){
        //注册
        UserDAO user = new UserDAO();
        BeanUtils.copyProperties(userDto, user);
        return userService.register(user);
    }
}
