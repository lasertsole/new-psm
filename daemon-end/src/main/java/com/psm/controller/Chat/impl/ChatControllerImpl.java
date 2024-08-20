package com.psm.controller.Chat.impl;

import com.psm.controller.Chat.ChatController;
import com.psm.service.Chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatControllerImpl implements ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/myServlet")
    public String testMyHttpServlet(){
        return "hello servlet";
    }

    @PostMapping("/myServlet1")
    public String testMyHttpServlet1(){
        return "hello servlet";
    }

    @GetMapping("/sendMsg")
    public String sendMsg(@RequestParam("userId") String userId, @RequestParam("msg") String msg){
        return "hello socketIO";
    }

    @GetMapping("/testSendMsg")
    public String testSendMsg(@RequestParam("username") String username,@RequestParam("msg") String msg){
        Map<String, Object> map = new HashMap<>();
        map.put("msg",msg);
        chatService.sendMessage(username, map);
        return "hello socketIO";
    }
}
