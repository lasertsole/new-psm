package com.psm.controller;

import com.psm.domain.DTO.UserDTO;
import com.psm.domain.DTO.ResponseDTO;
import com.psm.domain.User;
import com.psm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public ResponseDTO test(){
        return new ResponseDTO<>(HttpStatus.OK, "test");
    }

    @GetMapping("/testToken")
    public ResponseDTO testToken(){
        return new ResponseDTO<>(HttpStatus.OK, "test");
    }

    @PostMapping("/login")
    public ResponseDTO login(@Valid @RequestBody UserDTO userDto){
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return userService.login(user);
    }

    @GetMapping("/logout")
    public ResponseDTO logout()
    {
        return userService.logout();
    }

//    @PostMapping("/register")
//    public ResponseResult register(@RequestBody User user){
//        //注册
//        return userService.register(user);
//    }
}
