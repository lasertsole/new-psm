package com.psm.trigger.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.psm.domain.Communication.RTC.adaptor.RTCAdaptor;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.infrastructure.SocketIO.POJOs.Room;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import com.psm.infrastructure.SocketIO.properties.SocketAppProperties;
import com.psm.utils.Timestamp.TimestampUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

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
        SocketIONamespace RTCSignaling = socketIOServer.addNamespace("/RTC");

        // 添加校验token监听器
        RTCSignaling.addAuthTokenListener((authToken, client)->{
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
        RTCSignaling.addConnectListener(client -> {
            socketIOApi.addLocalUser(client.get("userId"), client);
        });

        // 添加创建房间监听器
        RTCSignaling.addEventListener("createRoom", String.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient srcClient, String roomId, AckRequest ackRequest) {
                try {
                    // 创建房间，并获取房间创建结果
                    Boolean isCreateSuccessful = socketIOApi.createSocketRoom(roomId, srcClient.get("userId"), "RTC", "DRTC");//DRTC为一对一RTC类型,可以后期更改为其他RTC类型

                    if(isCreateSuccessful) {
                        srcClient.set("rtcRoomId", roomId);
                    }

                    // 返回ack和消息接收时间戳
                    ackRequest.sendAckData(isCreateSuccessful);
                }
                catch (Exception e) {
                    ackRequest.sendAckData("server error");
                }
            }
        });

        // 添加邀请加入房间监听器
        RTCSignaling.addEventListener("inviteJoinRoom", String.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String tarUserId, AckRequest ackRequest) throws Exception {
                String rtcRoomId = (String) socketIOClient.get("rtcRoomId");
                Room room = socketIOApi.getSocketRoom(rtcRoomId);
                RoomInvitation roomInvitation = new RoomInvitation(rtcRoomId, room.getRoomOwnerId(), room.getRoomName(), room.getRoomType(), socketIOClient.get("userId"), tarUserId);
                rtcAdaptor.inviteJoinRoom(roomInvitation);

                // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                ackRequest.sendAckData(TimestampUtils.generateUTCTimestamp(formatter));
            }
        });

        RTCSignaling.addEventListener("agreeJoinRoom", String.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String tarUserId, AckRequest ackRequest) throws Exception {

            }
        });

        RTCSignaling.addEventListener("rejectJoinRoom", String.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String tarUserId, AckRequest ackRequest) throws Exception {

            }
        });

        RTCSignaling.addEventListener("swapSDP", Object.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest) throws Exception {

            }
        });

        RTCSignaling.addEventListener("swapCandidate", Object.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest) throws Exception {

            }
        });

        RTCSignaling.addEventListener("leaveRoom", String.class, new DataListener<>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {

            }
        });
    }
}
