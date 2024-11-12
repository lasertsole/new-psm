package com.psm.infrastructure.Websocket.Handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.websocket.OnMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BaseHandler {
    @Autowired
    private SocketIOServer socketIOServer;

    @PostConstruct
    private void startup() throws Exception {
        log.info("SocketIOServer启动成功");
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     *
     * @throws Exception 异常
     */
    @PreDestroy
    private void autoStop() throws Exception {
        socketIOServer.stop();
        log.info("SocketIOServer关闭成功");
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("客户端:" + client.getRemoteAddress() + "已连接");
        String namespace = client.getNamespace().getName();
        log.info("客户端:" + client.getRemoteAddress() + "已连接名字空间:" + namespace);
    }

    @OnMessage
    public void onEvent(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
        log.info("客户端:" + client.getRemoteAddress() + "发送消息:" + data);
        String namespace = client.getNamespace().getName();
        log.info("客户端:" + client.getRemoteAddress() + "已连接名字空间:" + namespace);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端:" + client.getRemoteAddress() + "断开连接");
    }
}
