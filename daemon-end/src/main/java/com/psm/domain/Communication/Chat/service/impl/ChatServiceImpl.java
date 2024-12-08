package com.psm.domain.Communication.Chat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Communication.Chat.entity.ChatBO;
import com.psm.domain.Communication.Chat.entity.ChatDO;
import com.psm.domain.Communication.Chat.entity.ChatDTO;
import com.psm.domain.Communication.Chat.repository.ChatDB;
import com.psm.domain.Communication.Chat.service.ChatService;
import com.psm.domain.Communication.Chat.types.convertor.ChatConvertor;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDB chatDB;

    @Autowired
    private MQPublisher mqPublisher;

    @Autowired
    private SocketAppProperties socketAppProperties;

    @Autowired
    private SocketIOApi socketIOApi;

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void processMessageDM(ChatBO message) {
        chatDB.insert(message.toDO());
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void patchInitMessage(SocketIOClient srcClient, Long userId, String timestamp) {
        Integer size = socketAppProperties.getDMMaxInitCountInPage();// 从环境配置中获取每页显示条数
        int current = 1;// 初始页码为1
        while(true) {
            Page<ChatDO> chatDOPage = chatDB.patchInitMessage(timestamp, userId, current, size);

            srcClient.sendEvent("initMessage",  ChatDTO.fromDOPage(chatDOPage));
            current ++;
            if (chatDOPage.getPages() < current) break;// 如果当前页码大于等于总页码，则跳出循环
        };
    };

    @Override
    public String sendMessage(SocketIOClient srcClient, ChatBO chatBO) throws ClientException {
        srcClient.set("DMLastTime", chatBO.getTimestamp());

        // 获取来源用户id
        String srcUserId = srcClient.get("userId");

        // 将消息发送到MQ
        mqPublisher.publish(chatBO, "DMForward", "CHAT", srcUserId);

        //生成返回时间戳(UTC国际化时间戳)
        return chatBO.generateTimestamp();
    }

    @Override
    public void receiveMessage(ChatBO chatBO) {
        // 如果本服务器存在目标用户socket，则把信息交付给目标用户
        SocketIOClient tgtClient = socketIOApi.getLocalUserSocket(String.valueOf(chatBO.getTgtUserId()));
        if (Objects.nonNull(tgtClient)){
            tgtClient.sendEvent("receiveMessage", ChatDTO.fromBO(chatBO));
        };
    }
}
