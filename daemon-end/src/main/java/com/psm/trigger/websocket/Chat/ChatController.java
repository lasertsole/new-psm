package com.psm.trigger.websocket.Chat;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.stereotype.Component;

@Component
public class ChatController {
//    @OnConnect
//    public void onConnect(SocketIOClient client) {
//        System.out.println("Client connected: " + client.getSessionId());
//    }
//
//    @OnDisconnect
//    public void onDisconnect(SocketIOClient client) {
//        System.out.println("Client disconnected: " + client.getSessionId());
//    }
//
//    @OnEvent(value = "myEvent")
//    public void onMyEvent(SocketIOClient client, AckRequest ackRequest, String data) {
//        System.out.println("Received event: " + data);
//        // 处理事件的逻辑
//    }
}
