package com.psm.trigger.socket.Chat;

import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.types.security.utils.JWT.JWTUtil;
import com.psm.infrastructure.Redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class OneToOneChatController implements CommandLineRunner {
    @Autowired
    UserAdaptor userAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JWTUtil jwtUtil;

    // 存储用户id和对应的socket的映射
    private final Map<String, SocketIOClient> userClientMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        SocketIONamespace oneToOneChat = socketIOServer.addNamespace("/OneToOneChat");
        oneToOneChat.addAuthTokenListener((authToken, client)->{// 校验token
            try{
                LinkedHashMap<String, Object> map = (LinkedHashMap) authToken;
                String userId = userAdaptor.authUserToken((String) map.get("token"));
                client.set("userId", userId);

                return AuthTokenResult.AuthTokenResultSuccess;
            }
            catch (Exception e){
                return new AuthTokenResult(false, "token无效:"+e.getCause());
            }
        });

        oneToOneChat.addConnectListener(client -> {
            userClientMap.put(client.get("userId"), client);

            log.info("客户端:" + client.getRemoteAddress() + "已连接OneToOne");
        });

        oneToOneChat.addEventListener("message", String.class, (client, data, ackRequest) -> {

        });

        oneToOneChat.addDisconnectListener(client ->{
            log.info("客户端:" + client.getRemoteAddress() + "断开连接名字空间");

            String userId = client.get("userId").toString();
            Optional.ofNullable(userId).ifPresent(userClientMap::remove);
        });
    }
}
