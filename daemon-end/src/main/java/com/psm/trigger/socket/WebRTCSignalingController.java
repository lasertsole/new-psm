package com.psm.trigger.socket;

import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.infrastructure.SocketIO.SocketIOGlobalVariable;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
public class WebRTCSignalingController implements CommandLineRunner {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private SocketAppProperties socketAppProperties;

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        SocketIONamespace webRTCSignaling = socketIOServer.addNamespace("/WebRTCSignaling");

        // 添加校验token监听器
        webRTCSignaling.addAuthTokenListener((authToken, client)->{
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
        webRTCSignaling.addConnectListener(client -> {
            SocketIOGlobalVariable.userIdMapClient.put(client.get("userId"), client);
        });

        // 添加断开连接监听器
        webRTCSignaling.addDisconnectListener(client -> {
            try {
                SocketIOGlobalVariable.userIdMapClient.remove(client.get("userId"));
            }
            catch (Exception e) {
                log.info("disconnect DM error");
            }
        });
    }
}
