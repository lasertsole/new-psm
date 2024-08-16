package com.psm.controller;

import com.psm.domain.UtilsDom.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @GetMapping("/test")
    public ResponseDTO test() {
        return new ResponseDTO(HttpStatus.OK, "上传成功");
    }
}
