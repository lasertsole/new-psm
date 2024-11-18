package com.psm.domain.Chat.service.impl;

import com.psm.domain.Chat.entity.ChatBO;
import com.psm.domain.Chat.repository.ChatDB;
import com.psm.domain.Chat.service.ChatService;
import com.psm.domain.Chat.types.convertor.ChatConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDB chatDB;

    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void processMessageDM(ChatBO message) {
        chatDB.insert(ChatConvertor.INSTANCE.BO2DO(message));
    }
}
