package com.psm.trigger.socket.Chat;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GroupChatController implements CommandLineRunner {
    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
//        // 添加加入房间的监听器
//        oneToOneChat.addEventListener("joinRoom", String.class, new DataListener<String>() {
//            @Override
//            public void onData(SocketIOClient client, String roomName, AckRequest ackRequest) throws Exception {
//                log.info("Client: " + client.getSessionId() + " is joining room: " + roomName);
//                client.joinRoom(roomName);
//                if (ackRequest.isAckRequested()) {
//                    ackRequest.sendAckData("Joined room: " + roomName);
//                }
//            }
//        });
//
//        // 添加离开房间的监听器
//        oneToOneChat.addEventListener("leaveRoom", String.class, new DataListener<String>() {
//            @Override
//            public void onData(SocketIOClient client, String roomName, AckRequest ackRequest) throws Exception {
//                log.info("Client: " + client.getSessionId() + " is leaving room: " + roomName);
//                client.leaveRoom(roomName);
//                if (ackRequest.isAckRequested()) {
//                    ackRequest.sendAckData("Left room: " + roomName);
//                }
//            }
//        });
//
//        // 添加发送消息的监听器
//        oneToOneChat.addEventListener("sendMessage", String.class, new DataListener<String>() {
//            @Override
//            public void onData(SocketIOClient client, String message, AckRequest ackRequest) throws Exception {
//                log.info("Client: " + client.getSessionId() + " sent message: " + message);
//
//                // 获取房间名称
//                String roomName = client.get("roomName").toString();
//                if (roomName != null && !roomName.isEmpty()) {
//                    // 向房间内的所有客户端广播消息
//                    oneToOneChat.getRoomOperations(roomName).sendEvent("message", message);
//                }
//
//                if (ackRequest.isAckRequested()) {
//                    ackRequest.sendAckData("Message sent: " + message);
//                }
//            }
//        });
    }
}
