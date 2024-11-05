package com.psm.domain.Chat.infrastructure.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * @Description:
 * @ClassName SocketIOMessageEventHandler
 * @date: 2021.09.09 09:06
 * @Author: zhanghang
 */
@Component
@Slf4j
public class SocketIOMessageEventHandler  extends Observable {

    @Autowired
    private SocketIOServer socketIoServer;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     *
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        try {
            socketIoServer.start();
        }catch (Exception ex){
            ex.printStackTrace();
            log.error("SocketIOServer启动失败");
        }
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     *
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception {
        socketIoServer.stop();
    }

    /**
     * 客户端连接的时候触发
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String username = client.getHandshakeData().getSingleUrlParam("username");
        // 这里可以传入token验证
        // 提醒观察者
        log.info("客户端:" + client.getRemoteAddress() + "  sessionId:" + client.getSessionId() +" username: "+ username+ "已连接");
    }

    /**
     * 客户端关闭连接时触发
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端:" + client.getSessionId() + "断开连接");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", "disconnect");
        paramMap.put("sessionId", client.getSessionId().toString());

        // 提醒观察者
        this.setChanged();
        this.notifyObservers(paramMap);
    }
}

