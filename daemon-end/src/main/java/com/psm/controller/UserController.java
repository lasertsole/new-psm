package com.psm.controller;

import com.psm.domain.ResponseResult;
import com.psm.domain.User;
import com.psm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public ResponseResult getUserState(){
        return new ResponseResult<>(200, "test");
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //登录
        return userService.login(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        //注册
        return userService.login(user);
    }
}
