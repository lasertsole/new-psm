package com.psm.domain.IndependentDomain.Communication.Chat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.IndependentDomain.Communication.Chat.entity.ChatBO;
import com.psm.domain.IndependentDomain.Communication.Chat.entity.ChatDO;
import com.psm.domain.IndependentDomain.Communication.Chat.entity.ChatDTO;
import com.psm.domain.IndependentDomain.Communication.Chat.repository.ChatDB;
import com.psm.domain.IndependentDomain.Communication.Chat.service.ChatService;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import com.psm.types.common.Event.Event;
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

    private final String namespace = "/DM";

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void storageMessageDM(ChatBO message) {
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
        // 设置用户最后发送时间
        srcClient.set("DMLastTime", chatBO.getTimestamp());

        // 刷新chatBO内的时间戳为到达服务器时间(UTC国际化时间戳)，并将时间戳返回给客户端
        String serverTimestamp = chatBO.generateServerTimestamp();

        // 如果本服务器存在目标用户socket，则把信息交付给目标用户
        SocketIOClient tgtClient = socketIOApi.getLocalUserSocket(namespace, String.valueOf(chatBO.getTgtUserId()));
        if (Objects.nonNull(tgtClient)){
            tgtClient.sendEvent("receiveMessage", ChatDTO.fromBO(chatBO));
            storageMessageDM(chatBO);
        } else {// 如果本服务器不存在目标用户socket，则把信息广播到MQ
            // 将消息发送到MQ，这里的chatBO时间戳已为到达服务器的时间戳.
            Event<ChatBO> event = new Event<>(chatBO, ChatBO.class);
            mqPublisher.publish(event, "DMForward", "CHAT");
        };

        //将时间戳返回给客户端
        return serverTimestamp;
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void receiveMessage(ChatBO chatBO) {
        // 如果本服务器存在目标用户socket，则把信息交付给目标用户
        SocketIOClient tgtClient = socketIOApi.getLocalUserSocket(namespace, String.valueOf(chatBO.getTgtUserId()));
        if (Objects.nonNull(tgtClient)){
            tgtClient.sendEvent("receiveMessage", ChatDTO.fromBO(chatBO));
        };
    }
}
