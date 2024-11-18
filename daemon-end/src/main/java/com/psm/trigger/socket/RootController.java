package com.psm.trigger.socket;

import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RootController implements CommandLineRunner {
    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        SocketIONamespace root = socketIOServer.addNamespace("/");

        // 添加校验token监听器
        root.addAuthTokenListener((authToken, client)->{
            client.disconnect();
            return new AuthTokenResult(false, "禁止连接根名字空间");
        });
    }
}
