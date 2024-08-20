package com.psm.controller.Common.impl;

import com.psm.controller.Common.CommonController;
import com.psm.domain.UtilsDom.ResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonControllerImpl implements CommonController {
    @GetMapping("/init")//登出
    public ResponseDTO init()
    {
        return null;
    }
}
