package com.psm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userState")
public class UserStateController {
    @GetMapping("/test")
    public String getUserState(){
        return "success";
    }
}
