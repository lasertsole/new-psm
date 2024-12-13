package com.psm.trigger.socket;

import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.entity.User.UserBO;
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
    private UserAdaptor userAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        SocketIONamespace security = socketIOServer.addNamespace("/security");

        // 添加校验token监听器
        security.addAuthTokenListener((authToken, client)->{
            try{
                Map<String, Object> map = (LinkedHashMap) authToken;
                UserBO userBO = userAdaptor.authUserToken((String) map.get("token"));
                client.set("userInfo", userBO);

                return AuthTokenResult.AuthTokenResultSuccess;
            }
            catch (Exception e){
                return new AuthTokenResult(false, "token无效:"+e.getCause());
            }
        });
    }
}
