package com.psm.trigger.socket;

import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

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
                UserBO userBO = userAdaptor.authUserToken((String) map.get("token"));
                client.set("userInfo", userBO);
                client.set("ip", client.getHandshakeData().getAddress().getHostName());
                log.info("用户登录成功, 用户ID: "+userBO.getId()+", IP: "+client.get("ip"));

                return AuthTokenResult.AuthTokenResultSuccess;
            }
            catch (Exception e){
                return new AuthTokenResult(false, "token无效:"+e.getCause());
            }
        });

        // 添加连接监听器
        security.addConnectListener(client -> {
            try {
                userAdaptor.socketLogin(client);
            } catch (Exception e) {
                return;
            }
        });

        // 添加断开连接监听器
        security.addDisconnectListener(client -> {
            // 移除用户在线用户列表
            socketIOApi.removeLocalUser(namespace, String.valueOf(((UserBO) client.get("userInfo")).getId()));
        });
    }
}
