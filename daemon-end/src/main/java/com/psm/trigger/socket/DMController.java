package com.psm.trigger.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.psm.domain.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Chat.entity.ChatBO;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.infrastructure.SocketIO.SocketIOGlobalVariable;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class DMController implements CommandLineRunner {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private ChatAdaptor chatAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private MQPublisher mqPublisher;

    @Autowired
    private SocketAppProperties socketAppProperties;

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        SocketIONamespace dm = socketIOServer.addNamespace("/DM");

        // 添加校验token监听器
        dm.addAuthTokenListener((authToken, client)->{
            try{
                Map<String, Object> map = (LinkedHashMap) authToken;
                String userId = userAdaptor.authUserToken((String) map.get("token"));
                client.set("userId", userId);

                return AuthTokenResult.AuthTokenResultSuccess;
            }
            catch (Exception e){
                return new AuthTokenResult(false, "token无效:"+e.getCause());
            }
        });

        // 添加连接监听器
        dm.addConnectListener(client -> {
            SocketIOGlobalVariable.userIdMapClient.put(client.get("userId"), client);

            // 向客户端发送配置信息
            Map<String, Object> map = new HashMap<>();
            map.put("DMExpireDay", socketAppProperties.getDMExpireDay());

            client.sendEvent("initDMConfig", map);
        });

        // 添加断开连接监听器
        dm.addDisconnectListener(client -> {
            try {
                SocketIOGlobalVariable.userIdMapClient.remove(client.get("userId"));
            }
            catch (Exception e) {
                log.info("disconnect DM error");
            }
        });

        // 添加私立聊监听器
        dm.addEventListener("sendMessage", ChatDTO.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, ChatDTO chatDTO, AckRequest ackRequest) {
                try {
                    ChatBO chatBO = ChatBO.fromDTO(chatDTO);
                    // 获取信息的发送时间戳，如果时间戳与上一次发信息相同，则证明是重复信息,直接丢弃.
                    if(Objects.nonNull(srcClient.get("DMLastTime")) && srcClient.get("DMLastTime").equals(chatBO.getTimestamp())) return;

                    String timestamp = chatAdaptor.sendMessage(srcClient, chatBO);

                    // 返回ack和消息接收时间戳
                    ackRequest.sendAckData(timestamp);
                }
                catch (ClientException e) {
                    ackRequest.sendAckData("MQ error");
                }
                catch (Exception e) {
                    ackRequest.sendAckData("server error");
                }
            }
        });

        dm.addEventListener("initMessage", String.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, String timestamp, AckRequest ackRequest) {
                try {
                    // 用户信息可能很庞大，需要异步处理
                    chatAdaptor.patchInitMessage(srcClient, timestamp);

                    // 返回ack和消息接收时间戳
                    ackRequest.sendAckData(timestamp);
                }
                catch (Exception e) {
                    ackRequest.sendAckData("server error");
                }
            }
        });
    }
}
