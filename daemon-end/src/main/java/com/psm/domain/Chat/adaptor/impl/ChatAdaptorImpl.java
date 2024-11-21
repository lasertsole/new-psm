package com.psm.domain.Chat.adaptor.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.corundumstudio.socketio.SocketIOClient;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Chat.entity.ChatBO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.psm.domain.Chat.service.ChatService;

import java.util.Objects;

@Adaptor
public class ChatAdaptorImpl implements ChatAdaptor {
    @Autowired
    private ChatService chatService;

    public void validateAndConvert(@Valid ChatBO chatBO) {
        if (
            Objects.isNull(chatBO.getTgtUserId())
            || Objects.isNull(chatBO.getSrcUserId())
            || StringUtils.isBlank(chatBO.getContent())
        ) {
            throw new IllegalArgumentException("Invalid parameter");
        }
    }

    @Override
    public void patchInitMessage(SocketIOClient srcClient, String timestamp) {
        chatService.patchInitMessage(srcClient, timestamp);
    };
}
