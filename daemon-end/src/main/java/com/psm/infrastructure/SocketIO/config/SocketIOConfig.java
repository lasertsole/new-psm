package com.psm.infrastructure.SocketIO.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@ConfigurationProperties(prefix = "socketio")
@Configuration
public class SocketIOConfig {
    private String host;
    private Integer port;
    private int bossCount;
    private int workCount;
    private boolean allowCustomRequests;
    private int upgradeTimeout;
    private int pingTimeout;
    private int pingInterval;

    /**
     * 以下配置在上面的application.properties中已经注明
     * @return SocketIOServer
     */
    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);// 支持在线人数
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);

        SocketIOServer server = new SocketIOServer(config);
        server.start(); // 启动socketIOServer

        return server;
    }

    /**
     * 用于扫描netty-socketio的注解，比如 @OnConnect、@OnEvent
     * 如果想要SocketIO 的注解生效，必须注入SpringAnnotationScanner 这个类
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner() {
        return new SpringAnnotationScanner(socketIOServer());
    }
}

