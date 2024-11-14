package com.psm.domain.Chat.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.Chat.service.ChatService;
import com.psm.infrastructure.Redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    // 存储用户id和对应的socket的映射在Redis的缓存时间，单位小时
    private static final Integer TIMEOUT = 1;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void connect(SocketIOClient client) {
        redisCache.setCacheObject((String) client.get("userId"), client, TIMEOUT, TimeUnit.HOURS);

        log.info("客户端:" + client.getRemoteAddress() + "已连接OneToOne");
    }

    @Override
    public void disconnect(SocketIOClient client) {
        redisCache.deleteObject((String) client.get("userId"));
    }

    @Override
    public void sendMessage(SocketIOClient client, ChatDTO message) {
        // 获取来源用户id
        String srcUserId = (String) client.get("userId");

        // 更新来源用户id对应的缓存过期时间
        redisCache.setCacheObject((String) client.get("userId"), client, TIMEOUT, TimeUnit.HOURS);

        // 获取目标用户id
        String tgtUserId = String.valueOf(message.getTgtUserId());

        // 获取目标用户对应的socket
        SocketIOClient tgtClient = redisCache.getCacheObject(tgtUserId);

        // 如果目标用户存在，则发送消息
        if (Objects.nonNull(tgtClient)){
            tgtClient.sendEvent("sendMessage", message);
        }
    }
}
