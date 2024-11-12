package com.psm.trigger.socket.Chat;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.transport.NamespaceClient;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.types.security.utils.JWT.JWTUtil;
import com.psm.infrastructure.Redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class OneToOneChatController implements CommandLineRunner {
    @Autowired
    UserAdaptor userAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    // 存储用户id和对应的socket的映射
    private final Map<String, SocketIOClient> userClientMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        SocketIONamespace oneToOneChat = socketIOServer.addNamespace("/OneToOneChat");

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
            userClientMap.put(client.get("userId"), client);

            log.info("客户端:" + client.getRemoteAddress() + "已连接OneToOne");
        });

        // 添加断开连接监听器
        oneToOneChat.addDisconnectListener(client ->{
            log.info("客户端:" + client.getRemoteAddress() + "断开连接名字空间");

            String userId = client.get("userId").toString();
            Optional.ofNullable(userId).ifPresent(userClientMap::remove);
        });

        // 添加加入房间的监听器
        oneToOneChat.addEventListener("joinRoom", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String roomName, AckRequest ackRequest) throws Exception {
                log.info("Client: " + client.getSessionId() + " is joining room: " + roomName);
                client.joinRoom(roomName);
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData("Joined room: " + roomName);
                }
            }
        });

        // 添加离开房间的监听器
        oneToOneChat.addEventListener("leaveRoom", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String roomName, AckRequest ackRequest) throws Exception {
                log.info("Client: " + client.getSessionId() + " is leaving room: " + roomName);
                client.leaveRoom(roomName);
                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData("Left room: " + roomName);
                }
            }
        });

        // 添加发送消息的监听器
        oneToOneChat.addEventListener("sendMessage", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String message, AckRequest ackRequest) throws Exception {
                log.info("Client: " + client.getSessionId() + " sent message: " + message);

                // 获取房间名称
                String roomName = client.get("roomName").toString();
                if (roomName != null && !roomName.isEmpty()) {
                    // 向房间内的所有客户端广播消息
                    oneToOneChat.getRoomOperations(roomName).sendEvent("message", message);
                }

                if (ackRequest.isAckRequested()) {
                    ackRequest.sendAckData("Message sent: " + message);
                }
            }
        });
    }
}
