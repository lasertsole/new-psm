package com.psm.domain.Independent.Communication.Single.RTC.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Independent.Communication.Single.RTC.service.RTCService;
import com.psm.domain.Independent.User.Single.user.entity.User.UserBO;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;
import com.psm.infrastructure.SocketIO.pojo.RTCSwap;
import com.psm.infrastructure.SocketIO.pojo.Room;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import com.psm.infrastructure.SocketIO.pojo.RoomInvitation;
import com.psm.infrastructure.SocketIO.enums.RoomTypeEnum;
import com.psm.types.common.Event.Event;
import com.psm.utils.Timestamp.TimestampUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class RTCServiceImpl implements RTCService {
    @Autowired
    private MQPublisher mqPublisher;

    @Autowired
    private SocketIOApi socketIOApi;

    private final String namespace = "/RTC";

    @Override
    public boolean createRoom(SocketIOClient srcClient, Room room) {
        String userId = String.valueOf(((UserBO) srcClient.get("userInfo")).getId());

        // 判断用户之前有没有加入其他rtc房间
        String oldRoomId = (String) srcClient.get("rtcRoomId");

        if (Objects.nonNull(oldRoomId)) { // 如果有，则将用户从原来的房间移除
            socketIOApi.removeUserFromSocketRoom(namespace, oldRoomId, userId);
            srcClient.del("rtcRoomId");// 清空房间号属性
        }

        boolean result = false;
        String newRoomId = room.getRoomId();
        // 创建房间，并获取房间创建结果,如果为true，则创建房间成功，为false，则说明已有相同的房间号被使用，创建失败
        if(socketIOApi.createSocketRoom(namespace, room)) {// 创建成功时设置用户的房间号属性
            srcClient.set("rtcRoomId", newRoomId);
            result = true;
        } else {// 创建失败时，则判断已有房间号的主人是否是当前用户，如果是，则用户可以直接使用该房间
            Room room1 = socketIOApi.getSocketRoom(namespace, newRoomId);
            if(userId.equals(room1.getRoomOwnerId()) && room1.getRoomType() == RoomTypeEnum.DRTC) {
                srcClient.set("rtcRoomId", newRoomId);
                result = true;
            };
        }
        return result;
    }

    @Override
    public String inviteJoinRoom(SocketIOClient srcClient, RoomInvitation roomInvitation) throws ClientException {
        String rtcRoomId = srcClient.get("rtcRoomId");
        if (Objects.isNull(rtcRoomId)) throw new ClientException("当前用户没有加入任何房间");

        // 将邀请函通过mq发送给目标用户
        Event<RoomInvitation> inviteJoinRoomEvent = new Event<>(roomInvitation, RoomInvitation.class);
        mqPublisher.publish(inviteJoinRoomEvent, "inviteJoinRoom", "RTC");

        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        return TimestampUtils.generateUTCTimestamp(formatter);
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void forwardInviteJoinRoom(RoomInvitation roomInvitation) {
        SocketIOClient tgtClient = socketIOApi.getLocalUserSocket(namespace, roomInvitation.getTgtUserId());
        if (Objects.isNull(tgtClient)) return;

        tgtClient.sendEvent("inviteJoinRoom", roomInvitation);
    }

    @Override
    public String agreeJoinRoom(SocketIOClient srcClient, RoomInvitation roomInvitation) throws ClientException {
        String roomId = roomInvitation.getRoomId();
        String userId = String.valueOf(((UserBO) srcClient.get("userInfo")).getId());

        // 判断要加入的房间是否还存在,如果不存在，则抛出异常
        Room socketRoom = socketIOApi.getSocketRoom(namespace, roomId);
        if (Objects.isNull(socketRoom)) throw new ClientException("房间不存在");

        // 如果房间是DRTC(一对一)类型,且房间人数大于等于2人，且成员不包含自己，则说明房间已满，抛出异常(也用于保证操作的幂等性)
        Set<String> memberIdSet = socketRoom.getMemberIdSet();
        if (socketRoom.getRoomType() == RoomTypeEnum.DRTC && memberIdSet.size()>=2 && !memberIdSet.contains(userId) ) throw new ClientException("房间已满");

        // 如果当前用户已加入rtc房间，并且房间号和邀请函的房间号不同,则退出已加入的房间
        String joinedRoomId = (String) srcClient.get("rtcRoomId");
        if (Objects.nonNull(joinedRoomId) && !joinedRoomId.equals(roomId)) {
            socketIOApi.removeUserFromSocketRoom(namespace, joinedRoomId, userId);// 退出已加入的房间
            socketIOApi.addUserToSocketRoom(namespace, roomId, userId);// 当前用户加入目标房间
            srcClient.set("rtcRoomId", roomId);// 重置用户的房间号属性
        };

        // 将加入房间的信息，通知该房间的其他用户
        Event<RoomInvitation> agreeJoinRoomEvent = new Event<>(roomInvitation, RoomInvitation.class);
        mqPublisher.publish(agreeJoinRoomEvent, "agreeJoinRoom", "RTC");

        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return TimestampUtils.generateUTCTimestamp(formatter);
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void forwardAgreeJoinRoom(RoomInvitation roomInvitation) {
        // 从Cache中获取出房间的所有用户
        Room socketRoom = socketIOApi.getSocketRoom(namespace, roomInvitation.getRoomId());

        // 找出本服务器上在房间内的用户并进行通知
        socketRoom.getMemberIdSet().forEach(userId -> {
            SocketIOClient socketIOClient = socketIOApi.getLocalUserSocket(namespace, userId);
            if (Objects.isNull(socketIOClient)) return;
            socketIOClient.sendEvent("agreeJoinRoom", roomInvitation);
        });
    }

    @Override
    public String rejectJoinRoom(SocketIOClient srcClient, RoomInvitation roomInvitation) throws ClientException {
        // 将拒绝邀请的信息转发给邀请人
        Event<RoomInvitation> rejectJoinRoomEvent = new Event<>(roomInvitation, RoomInvitation.class);
        mqPublisher.publish(rejectJoinRoomEvent, "rejectJoinRoom", "RTC");

        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return TimestampUtils.generateUTCTimestamp(formatter);
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void forwardRejectJoinRoom(RoomInvitation roomInvitation) {
        // 找出本服务器上在邀请的用户并进行通知
        SocketIOClient localUserSocket = socketIOApi.getLocalUserSocket(namespace, roomInvitation.getSrcUserId());

        // 如果用户不在本服务器上，则直接返回
        if (Objects.isNull(localUserSocket)) return;

        // 将被邀请者拒绝邀请的信息通知邀请者
        localUserSocket.sendEvent("rejectJoinRoom", roomInvitation);

        // 如果房间是DRTC(一对一)类型,则直接删除该房间
        Room socketRoom = socketIOApi.getSocketRoom(namespace, roomInvitation.getRoomId());//获取房间类型,从Cache拿房间类型而不是从roomInvitation变量拿,防止被邀请方伪造房间类型
        RoomTypeEnum roomType = socketRoom.getRoomType();
        if (roomType == RoomTypeEnum.DRTC) {
            socketIOApi.destroySocketRoom(namespace, roomInvitation.getRoomId());
        };
    }

    @Override
    public String swapSDP(SocketIOClient socketIOClient, RTCSwap rtcSwap) throws ClientException {
        // 将交换swap的信息转发给房间成员
        Event<RTCSwap> swapSDPEvent = new Event<>(rtcSwap, RTCSwap.class);
        mqPublisher.publish(swapSDPEvent, "swapSDP", "RTC");

        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return TimestampUtils.generateUTCTimestamp(formatter);
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void forwardSwapSDP(RTCSwap rtcSwap) {
        // 如果本服务器有目标对象，则把信息交付给目标用户
        SocketIOClient tgtClient = socketIOApi.getLocalUserSocket(namespace, rtcSwap.getTgtUserId());
        if (Objects.isNull(tgtClient)) return;

        tgtClient.sendEvent("swapSDP", rtcSwap);
    }

    @Override
    public String swapCandidate(SocketIOClient socketIOClient, RTCSwap rtcSwap) throws ClientException {
        // 将交换swap的信息转发给房间成员
        Event<RTCSwap> swapCandidateEvent = new Event<>(rtcSwap, RTCSwap.class);
        mqPublisher.publish(swapCandidateEvent, "swapCandidate", "RTC");

        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return TimestampUtils.generateUTCTimestamp(formatter);
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void forwardSwapCandidate(RTCSwap rtcSwap) {
        // 如果本服务器有目标对象，则把信息交付给目标用户
        SocketIOClient tgtClient = socketIOApi.getLocalUserSocket(namespace, rtcSwap.getTgtUserId());
        if (Objects.isNull(tgtClient)) return;

        tgtClient.sendEvent("swapCandidate", rtcSwap);
    }

    @Override
    public String leaveRoom(SocketIOClient srcClient, RTCSwap rtcSwap) throws ClientException {
        // 从srcClient中获取用户id
        String userId = String.valueOf(((UserBO) srcClient.get("userInfo")).getId());

        // 将本用户从房间中移除
        socketIOApi.removeUserFromSocketRoom(namespace, srcClient.get("rtcRoomId"), userId);

        // 删除用户的房间标识符
        srcClient.del("rtcRoomId");

        // 将交换swap的信息转发给邀请人
        Event<RTCSwap> leaveRoomEvent = new Event<>(rtcSwap, RTCSwap.class);
        mqPublisher.publish(leaveRoomEvent, "leaveRoom", "RTC");

        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return TimestampUtils.generateUTCTimestamp(formatter);
    }

    @Override
    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void forwardLeaveRoom(RTCSwap rtcSwap) {
        // 从Cache中获取出房间的所有用户
        Room socketRoom = socketIOApi.getSocketRoom(namespace, rtcSwap.getRoomId());

        // 如果房间已不存在，则返回
        if(Objects.isNull(socketRoom)) return;

        // 找出本服务器上在房间内的用户并进行通知
        socketRoom.getMemberIdSet().forEach(userId -> {
            SocketIOClient tarClient = socketIOApi.getLocalUserSocket(namespace, userId);
            if (Objects.isNull(tarClient)) return;
            tarClient.sendEvent("leaveRoom", rtcSwap);
        });
    }
}
