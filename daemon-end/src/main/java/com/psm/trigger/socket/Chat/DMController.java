package com.psm.trigger.socket.Chat;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.psm.domain.Chat.adaptor.ChatAdaptor;
import com.psm.domain.Chat.entity.ChatDTO;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
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

    // 存储用户id和对应的socket的映射
    private final Map<String, SocketIOClient> userClientMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        SocketIONamespace oneToOneChat = socketIOServer.addNamespace("/DM");

        // 添加校验token监听器
        oneToOneChat.addAuthTokenListener((authToken, client)->{
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
        oneToOneChat.addConnectListener(client -> {
            try {
                chatAdaptor.connectDM(client);
            }
            catch (Exception e) {
                log.info("connect DM error");
            }
        });

        // 添加断开连接监听器
        oneToOneChat.addDisconnectListener(client ->{
            try {
                chatAdaptor.disconnectDM(client);
            }
            catch (Exception e) {
                log.info("disconnect DM error");
            }
        });

        // 添加私立聊监听器
        oneToOneChat.addEventListener("sendMessage", ChatDTO.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient client, ChatDTO message, AckRequest ackRequest) {
                try {
                    chatAdaptor.sendMessageDM(client, message);
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
