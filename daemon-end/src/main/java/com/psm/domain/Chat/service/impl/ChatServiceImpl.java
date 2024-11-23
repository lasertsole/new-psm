package com.psm.domain.Chat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Chat.entity.ChatBO;
import com.psm.domain.Chat.entity.ChatDO;
import com.psm.domain.Chat.entity.ChatVO;
import com.psm.domain.Chat.repository.ChatDB;
import com.psm.domain.Chat.service.ChatService;
import com.psm.domain.Chat.types.convertor.ChatConvertor;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDB chatDB;

    @Autowired
    private SocketAppProperties socketAppProperties;

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void processMessageDM(ChatBO message) {
        chatDB.insert(ChatConvertor.INSTANCE.BO2DO(message));
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void patchInitMessage(SocketIOClient srcClient, Long userId, String timestamp) {

        Integer size = socketAppProperties.getDMMaxInitCountInPage();// 从环境配置中获取每页显示条数
        int current = 1;// 初始页码为1
        while(true) {
            Page<ChatDO> chatDOPage = chatDB.patchInitMessage(timestamp, userId, current, size);

            List<ChatVO> chatVOs = chatDOPage.getRecords().stream().map(ChatConvertor.INSTANCE::DO2VO).toList();

            srcClient.sendEvent("initMessage",  chatVOs, current);
            current ++;
            if (chatDOPage.getPages() < current) break;// 如果当前页码大于等于总页码，则跳出循环
        };
    };
}
