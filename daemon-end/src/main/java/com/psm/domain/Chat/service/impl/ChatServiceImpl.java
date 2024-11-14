package com.psm.domain.Chat.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.Chat.repository.ChatDB;
import com.psm.domain.Chat.service.ChatService;
import com.psm.domain.Chat.types.convertor.ChatConvertor;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDB chatDB;

    @Autowired
    private MQPublisher mqPublisher;

    // 存储用户id和对应的socket的映射（因为websocket连接在本地主机，所以不需要考虑多节点问题）
    Map<String, SocketIOClient> userIdMapClient = new ConcurrentHashMap<>();

    @Override
    public void connectDM(SocketIOClient client) {
        userIdMapClient.put(client.get("userId"), client);

        log.info("客户端:" + client.getRemoteAddress() + "已连接DM");
    }

    @Override
    public void disconnectDM(SocketIOClient client) {
        userIdMapClient.remove(client.get("userId"));
    }

    @Override
    public void sendMessageDM(SocketIOClient srcClient, ChatDTO message) throws ClientException {
        // 获取来源用户id
        String srcUserId = srcClient.get("userId");

        // 获取目标用户id
        String tgtUserId = String.valueOf(message.getTgtUserId());

        // 如果目标用户存在，则发送消息
        SocketIOClient tgtClient = userIdMapClient.get(tgtUserId);
        if (Objects.nonNull(tgtClient)){
            tgtClient.sendEvent("sendMessage", message);
        }

        // 将消息发送到MQ
        mqPublisher.publish(message, "DM", "forwardMS", srcUserId);
    }

    @Async("ThreadPoolTaskExecutor")// 使用有界异步线程池处理该方法
    public void processMessageDM(ChatDTO message) {
        chatDB.insert(ChatConvertor.INSTANCE.DTO2DAO(message));
    }
}
