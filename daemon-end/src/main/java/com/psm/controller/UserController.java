package com.psm.controller;

import com.psm.domain.ResponseResult;
import com.psm.domain.User;
import com.psm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public ResponseResult test(){
        return new ResponseResult<>(HttpStatus.OK, "test");
    }

    @GetMapping("/testToken")
    public ResponseResult testToken(){
        return new ResponseResult<>(HttpStatus.OK, "test");
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return userService.login(user);
    }

    @GetMapping("/logout")
    public ResponseResult logout()
    {
        return userService.logout();
    }

//    @PostMapping("/register")
//    public ResponseResult register(@RequestBody User user){
//        //注册
//        return userService.register(user);
//    }
}
