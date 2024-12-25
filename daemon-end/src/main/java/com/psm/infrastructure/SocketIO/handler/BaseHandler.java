package com.psm.infrastructure.SocketIO.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BaseHandler {
    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private SocketIOApi socketIOApi;

    private final String namespace = "/";

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
        // 将全局客户端加入本地缓存
        socketIOApi.addLocalUser(namespace, client.getSessionId().toString(), client);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        // 将全局客户端移除本地缓存
        socketIOApi.removeLocalUser(namespace, client.getSessionId().toString());
    }
}
