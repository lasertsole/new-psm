package com.psm.trigger.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.psm.domain.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Chat.entity.ChatBO;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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

    // 存储用户id和对应的socket的映射（因为websocket连接在本地主机，所以不需要考虑多节点问题）
    private final Map<String, SocketIOClient> userIdMapClient = new ConcurrentHashMap<>();

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
            try {
                userIdMapClient.put(client.get("userId"), client);
            }
            catch (Exception e) {
                log.info("connect DM error");
            }
        });

        // 添加断开连接监听器
        dm.addDisconnectListener(client ->{
            try {
                userIdMapClient.remove(client.get("userId"));
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
                    // 校验参数
                    ChatBO chatBO = ChatBO.fromDTO(chatDTO);

                    // 获取信息的发送时间戳，如果时间戳与上一次发信息相同，则证明是重复信息
                    if(Objects.nonNull(srcClient.get("DMLastTime")) && srcClient.get("DMLastTime").equals(chatDTO.getTimestamp())) return;
                    srcClient.set("DMLastTime", chatDTO.getTimestamp());

                    // 获取来源用户id
                    String srcUserId = srcClient.get("userId");

                    // 获取目标用户id
                    String tgtUserId = String.valueOf(chatBO.getTgtUserId());

                    // 如果目标用户存在，则发送消息
                    SocketIOClient tgtClient = userIdMapClient.get(tgtUserId);
                    if (Objects.nonNull(tgtClient)){
                        tgtClient.sendEvent("receiveMessage", chatDTO.toVO());
                    }

                    //生成返回时间戳(UTC国际化时间戳)
                    String timestamp = chatBO.generateTimestamp();

                    // 将消息发送到MQ
                    mqPublisher.publish(chatBO, "DMForward", "CHAT", srcUserId);

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
    }
}
