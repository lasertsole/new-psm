package com.psm.trigger.socket.Root;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class RootController implements CommandLineRunner {
    @Autowired
    private SocketIOServer socketIOServer;

    // 创建一个名字空间
    SocketIONamespace oneToOneChat = socketIOServer.addNamespace("/");

    @Override
    public void run(String... args) throws Exception {

    }
}
