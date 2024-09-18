package com.psm.application;

import com.psm.infrastructure.utils.VO.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {
    @GetMapping("/init")//登出
    public ResponseVO init()
    {
        return null;
    }
}
