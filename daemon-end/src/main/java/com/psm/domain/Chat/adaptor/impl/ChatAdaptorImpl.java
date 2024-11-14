package com.psm.domain.Chat.adaptor.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Chat.entity.ChatDTO;
import jakarta.validation.Valid;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import com.psm.domain.Chat.service.ChatService;

@Adaptor
public class ChatAdaptorImpl implements ChatAdaptor {
    @Autowired
    private ChatService chatService;

    @Override
    public void connectDM(SocketIOClient client) {
        chatService.connectDM(client);
    }

    @Override
    public void disconnectDM(SocketIOClient client) {
        chatService.disconnectDM(client);
    }

    @Override
    public void sendMessageDM(SocketIOClient client, @Valid ChatDTO message) throws ClientException {
        chatService.sendMessageDM(client, message);
    }
}
