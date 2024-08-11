package com.psm.controller;

import com.psm.domain.UtilsDom.ResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {
    @GetMapping("/init")//登出
    public ResponseDTO init()
    {
        return null;
    }
}
