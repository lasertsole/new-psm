package com.psm.domain.Chat.adaptor.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Chat.entity.ChatBO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import com.psm.domain.Chat.service.ChatService;

import java.util.Objects;

@Adaptor
public class ChatAdaptorImpl implements ChatAdaptor {
    @Autowired
    private ChatService chatService;
    @Override
    public void patchInitMessage(SocketIOClient srcClient, String timestamp) {
        chatService.patchInitMessage(srcClient, Long.parseLong(srcClient.get("userId")), timestamp);
    };

    @Override
    public String sendMessage(SocketIOClient srcClient, @Valid ChatBO chatBO) throws ClientException {
        chatBO.setContent(StringEscapeUtils.escapeHtml4(chatBO.getContent())); // 防止XSS攻击
        return chatService.sendMessage(srcClient, chatBO);
    };
}
