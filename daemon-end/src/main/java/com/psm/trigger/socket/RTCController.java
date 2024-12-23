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
        SocketIONamespace rtc = socketIOServer.addNamespace(namespace);

        // 添加校验token监听器
        rtc.addAuthTokenListener((authToken, client)-> {
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
        rtc.addConnectListener(client -> {
            try {
                // 添加本地用户
                socketIOApi.addLocalUser(namespace, String.valueOf(((UserBO) client.get("userInfo")).getId()), client);
            } catch (Exception e) {
                log.error("connection error: {}", e.getMessage());
            }
        });

        rtc.addDisconnectListener(client -> {
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
                    log.error("disconnection error: {}", e.getMessage());
                }
            }
        });

        // 添加创建房间监听器
        rtc.addEventListener("createRoom", Room.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, Room room, AckRequest ackRequest) {
                try {
                    boolean result = rtcAdaptor.createRoom(srcClient, room);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(result);
                } catch (Exception e) {
                    log.error("create room error: {}", e.getMessage());
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        // 添加邀请加入房间监听器
        rtc.addEventListener("inviteJoinRoom", RoomInvitation.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RoomInvitation roomInvitation, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.inviteJoinRoom(srcClient, roomInvitation);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (ClientException e) {
                    ackRequest.sendAckData("room is not exits");
                } catch (Exception e) {
                    log.error("invite join room error: {}", e.getMessage());
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        // 添加同意加入房间监听器
        rtc.addEventListener("agreeJoinRoom", RoomInvitation.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RoomInvitation roomInvitation, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.agreeJoinRoom(srcClient, roomInvitation);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    log.error("agree join room error: {}", e.getMessage());
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        // 添加拒绝加入房间监听器
        rtc.addEventListener("rejectJoinRoom", RoomInvitation.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RoomInvitation roomInvitation, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.rejectJoinRoom(srcClient, roomInvitation);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    log.error("reject join room error: {}", e.getMessage());
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        // 添加交换SDP监听器
        rtc.addEventListener("swapSDP", RTCSwap.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RTCSwap rtcSwap, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.swapSDP(srcClient, rtcSwap);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    log.error("server error :{}", e.getMessage());
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        // 添加交换Candidate监听器
        rtc.addEventListener("swapCandidate", RTCSwap.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RTCSwap rtcSwap, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.swapCandidate(srcClient, rtcSwap);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    log.error("server error :{}", e.getMessage());
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });

        // 添加离开房间监听器
        rtc.addEventListener("leaveRoom", RTCSwap.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, RTCSwap rtcSwap, AckRequest ackRequest) throws Exception {
                try {
                    String timestamp =  rtcAdaptor.swapCandidate(srcClient, rtcSwap);

                    // 返回创建房间的结果
                    ackRequest.sendAckData(timestamp);
                } catch (Exception e) {
                    log.error("server error :{}", e.getMessage());
                    ackRequest.sendAckData("server error " + e.getCause());
                }
            }
        });
    }
}
