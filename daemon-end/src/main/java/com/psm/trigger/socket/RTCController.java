package com.psm.trigger.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.psm.domain.Communication.RTC.adaptor.RTCAdaptor;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.SocketIO.POJOs.RTCSwap;
import com.psm.infrastructure.SocketIO.POJOs.Room;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class RTCController implements CommandLineRunner {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private RTCAdaptor rtcAdaptor;

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private SocketAppProperties socketAppProperties;

    @Autowired
    private SocketIOApi socketIOApi;

    @Override
    public void run(String... args) throws Exception {
        // 创建一个名字空间
        String namespace = "/RTC";
        SocketIONamespace RTCSignaling = socketIOServer.addNamespace(namespace);

        // 添加校验token监听器
        RTCSignaling.addAuthTokenListener((authToken, client)-> {
            try{
                Map<String, Object> map = (LinkedHashMap) authToken;
                UserBO userBO = userAdaptor.authUserToken((String) map.get("token"));
                client.set("userInfo", userBO);

                return AuthTokenResult.AuthTokenResultSuccess;
            }
            catch (Exception e){
                return new AuthTokenResult(false, "token无效: "+e.getCause());
            }
        });

        // 添加连接监听器
        RTCSignaling.addConnectListener(client -> {
            // 添加本地用户
            socketIOApi.addLocalUser(namespace, String.valueOf(((UserBO) client.get("userInfo")).getId()), client);
        });

        RTCSignaling.addDisconnectListener(client -> {
            // 移除用户在线用户列表
            socketIOApi.removeLocalUser(namespace, String.valueOf(((UserBO) client.get("userInfo")).getId()));

            // 如果用户有房间号，则 调用离开房间函数
            String roomId = (String) client.get("rtcRoomId");
            if(Objects.nonNull(roomId)) {
                UserBO userBO = (UserBO) client.get("userInfo");
                RTCSwap rtcSwap = new RTCSwap();
                rtcSwap.setRoomId(roomId);
                rtcSwap.setSrcUserId(String.valueOf(userBO.getId()));
                rtcSwap.setSrcUserName(String.valueOf(userBO.getName()));

                try {
                    rtcAdaptor.leaveRoom(client, rtcSwap);
                } catch (ClientException e) {
                    return;
                }
            }
        });

        // 添加创建房间监听器
        RTCSignaling.addEventListener("createRoom", Room.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, Room room, AckRequest ackRequest) {
                try {
                    boolean result = rtcAdaptor.createRoom(srcClient, room);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(result);
                } catch (Exception e) {
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        // 添加邀请加入房间监听器
        RTCSignaling.addEventListener("inviteJoinRoom", RoomInvitation.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RoomInvitation roomInvitation, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.inviteJoinRoom(srcClient, roomInvitation);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        RTCSignaling.addEventListener("agreeJoinRoom", RoomInvitation.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RoomInvitation roomInvitation, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.agreeJoinRoom(srcClient, roomInvitation);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        RTCSignaling.addEventListener("rejectJoinRoom", RoomInvitation.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RoomInvitation roomInvitation, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.rejectJoinRoom(srcClient, roomInvitation);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        RTCSignaling.addEventListener("swapSDP", RTCSwap.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RTCSwap rtcSwap, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.swapSDP(srcClient, rtcSwap);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        RTCSignaling.addEventListener("swapCandidate", RTCSwap.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RTCSwap rtcSwap, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.swapCandidate(srcClient, rtcSwap);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        RTCSignaling.addEventListener("leaveRoom", RTCSwap.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RTCSwap rtcSwap, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.swapCandidate(srcClient, rtcSwap);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });
    }
}
