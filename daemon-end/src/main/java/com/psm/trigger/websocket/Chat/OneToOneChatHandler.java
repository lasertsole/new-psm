package com.psm.trigger.websocket.Chat;

import com.corundumstudio.socketio.AuthTokenResult;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.psm.domain.User.user.entity.LoginUser.LoginUser;
import com.psm.domain.User.user.types.security.utils.JWT.JWTUtil;
import com.psm.infrastructure.Redis.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class OneToOneChatHandler implements CommandLineRunner {
    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void run(String... args) throws Exception {
        SocketIONamespace oneToOneChat = socketIOServer.addNamespace("/OneToOneChat");
        oneToOneChat.addAuthTokenListener((token, client)->{
//            //解析token
//            String userid;
//            try {
//                Claims claims = jwtUtil.parseJWT(token);
//                userid = claims.getSubject();
//            } catch (Exception e) {
//                unAuthenticatedQueue.get(client).cancel(true);
//                unAuthenticatedQueue.remove(client);
//                client.disconnect();
//                return;
//            }
//
//            //从redis中获取用户信息
//            String redisKey = "login:" + userid;
//            LoginUser loginUser = redisCache.getCacheObject(redisKey);
//
//            if(Objects.isNull(loginUser)){
//                unAuthenticatedQueue.get(client).cancel(true);
//                unAuthenticatedQueue.remove(client);
//                client.disconnect();
//                return;
//            }

            return AuthTokenResult.AuthTokenResultSuccess;
        });

        oneToOneChat.addConnectListener(client -> {
            log.info("客户端:" + client.getRemoteAddress() + "已连接OneToOne");
        });

        oneToOneChat.addEventListener("message", String.class, (client, data, ackRequest) -> {
//            ackRequest.isAckRequested();
//            client.sendEvent("message", data);
        });

        oneToOneChat.addDisconnectListener(client ->{
            log.info("客户端:" + client.getRemoteAddress() + "断开连接名字空间");
        });
    }
}
