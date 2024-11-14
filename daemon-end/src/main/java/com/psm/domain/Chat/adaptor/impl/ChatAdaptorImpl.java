package com.psm.domain.Chat.adaptor.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Chat.entity.ChatDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.psm.domain.Chat.service.ChatService;

@Adaptor
public class ChatAdaptorImpl implements ChatAdaptor {
    @Autowired
    private ChatService chatService;

    @Override
    public void connect(SocketIOClient client) {
        chatService.connect(client);
    }

    @Override
    public void disconnect(SocketIOClient client) {
        chatService.disconnect(client);
    }

    @Override
    public void sendMessage(SocketIOClient client, @Valid ChatDTO message) {
        chatService.sendMessage(client, message);
    }
}
