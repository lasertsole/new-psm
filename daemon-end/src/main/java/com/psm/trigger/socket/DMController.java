package com.psm.trigger.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.psm.domain.Communication.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Communication.Chat.entity.ChatBO;
import com.psm.domain.Communication.Chat.entity.ChatDTO;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class DMController implements CommandLineRunner {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private ChatAdaptor chatAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private SocketAppProperties socketAppProperties;

    @Autowired
    private SocketIOApi socketIOApi;

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        SocketIONamespace dm = socketIOServer.addNamespace("/DM");

        // 添加校验token监听器
        dm.addAuthTokenListener((authToken, client)->{
            try{
                Map<String, Object> map = (LinkedHashMap) authToken;
                UserBO userBO = userAdaptor.authUserToken((String) map.get("token"));
                client.set("userInfo", userBO);

                return AuthTokenResult.AuthTokenResultSuccess;
            }
            catch (Exception e){
                return new AuthTokenResult(false, "token无效:"+e.getCause());
            }
        });

        // 添加连接监听器
        dm.addConnectListener(client -> {
            // 添加本地用户
            socketIOApi.addLocalUser(String.valueOf(((UserBO) client.get("userInfo")).getId()), client);

            // 向客户端发送配置信息
            Map<String, Object> map = new HashMap<>();
            map.put("DMExpireDay", socketAppProperties.getDMExpireDay());

            // 发送配置信息
            client.sendEvent("initDMConfig", map);
        });

        // 添加私聊监听器
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
