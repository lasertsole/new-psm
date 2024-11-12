package com.psm.trigger.socket.Chat;

import com.corundumstudio.socketio.SocketIOServer;
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

    }
}
