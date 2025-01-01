package com.psm.trigger.socket;

import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.psm.domain.Independent.User.Single.user.adaptor.UserAdaptor;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserBO;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class SecurityController  implements CommandLineRunner {
    @Autowired
    private SocketIOApi socketIOApi;

    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        String namespace = "/security";
        SocketIONamespace security = socketIOServer.addNamespace(namespace);

        // 添加校验token监听器
        security.addAuthTokenListener((authToken, client)->{
            try{
                Map<String, Object> map = (LinkedHashMap) authToken;
                String token = (String) map.get("token");
                if (Objects.isNull(token))
                    return new AuthTokenResult(false, "Invalid parameter");

                UserBO userBO = userAdaptor.authUserToken(token);
                client.set("userInfo", userBO);

                return AuthTokenResult.AuthTokenResultSuccess;
            }
            catch (Exception e){
                return new AuthTokenResult(false, "Invalid token:"+e.getCause());
             }
        });

        // 添加连接监听器
        security.addConnectListener(client -> {
            try {
                userAdaptor.socketLogin(client);
            } catch (Exception e) {
                log.error("connection error: {}", e.getMessage());
            }
        });

        // 添加断开连接监听器
        security.addDisconnectListener(client -> {
            try {
                // 移除用户在线用户列表
                socketIOApi.removeLocalUser(namespace, String.valueOf(((UserBO) client.get("userInfo")).getId()));
            } catch (Exception e) {
                log.error("disconnect error: {}", e.getMessage());
            }
        });
    }
}
