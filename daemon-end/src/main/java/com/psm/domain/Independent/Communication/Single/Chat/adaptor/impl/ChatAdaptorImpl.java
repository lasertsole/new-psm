package com.psm.domain.Independent.Communication.Single.Chat.adaptor.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Independent.Communication.Single.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Independent.Communication.Single.Chat.entity.ChatBO;
import com.psm.domain.Independent.User.Single.user.entity.User.UserBO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import com.psm.domain.Independent.Communication.Single.Chat.service.ChatService;

@Slf4j
@Adaptor
public class ChatAdaptorImpl implements ChatAdaptor {
    @Autowired
    private ChatService chatService;
    @Override
    public void patchInitMessage(SocketIOClient srcClient, String timestamp) {
        chatService.patchInitMessage(srcClient, ((UserBO) srcClient.get("userInfo")).getId(), timestamp);
    };

    @Override
    public String sendMessage(SocketIOClient srcClient, @Valid ChatBO chatBO) throws ClientException {
        chatBO.setContent(StringEscapeUtils.escapeHtml4(chatBO.getContent())); // 防止XSS攻击

        return chatService.sendMessage(srcClient, chatBO);
    };
}
