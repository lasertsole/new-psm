package com.psm.service.Chat.impl;

import com.psm.service.Chat.ChatManagerService;
import com.psm.service.Chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatManagerServiceImpl implements ChatManagerService {
    @Autowired
    private ChatService chatService;
}
