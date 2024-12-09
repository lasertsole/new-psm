package com.psm.domain.Communication.RTC.adaptor;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.infrastructure.SocketIO.POJOs.RTCSwap;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import org.apache.rocketmq.client.apis.ClientException;

public interface RTCAdaptor {
    /**
     * 创建房间
     *
     * @param srcClient 创建房间的用户客户端
     * @param roomId 房间id
     */

    boolean createRoom(SocketIOClient srcClient, String roomId);

    /**
     * 邀请用户加入房间
     *
     * @param srcClient 主动邀请的用户id客户端
     * @param tarUserId 被动邀请的用户id
     */
    String inviteJoinRoom(SocketIOClient srcClient, String tarUserId) throws ClientException;

    /**
     * 用户同意加入房间
     *
     * @param srcClient 主动邀请的用户id客户端
     * @param roomInvitation 邀请函
     * @return 同意动作的时间戳
     */
    String agreeJoinRoom(SocketIOClient srcClient, RoomInvitation roomInvitation) throws ClientException;

    /**
     * 用户拒绝加入房间
     *
     * @param srcClient 主动邀请的用户id客户端
     * @param roomInvitation 房间邀请函
     * @return 同意动作的时间戳
     */
    String rejectJoinRoom(SocketIOClient srcClient, RoomInvitation roomInvitation) throws ClientException;

    /**
     * 交换SDP
     *
     * @param socketIOClient 发送者客户端
     * @param rtcSwap RTC元数据
     * @return 交换SDP的时间戳
     */
    String swapSDP(SocketIOClient socketIOClient, RTCSwap rtcSwap) throws ClientException;

    /**
     * 交换Candidate
     *
     * @param socketIOClient 发送者客户端
     * @param rtcSwap RTC元数据
     * @return 交换ICE Candidate的时间戳
     */
    String swapCandidate(SocketIOClient socketIOClient, RTCSwap rtcSwap) throws ClientException;

    /**
     * 离开房间
     *
     * @param socketIOClient 离开房间的用户客户端
     * @param rtcSwap RTC元数据
     * @return 离开房间的时间戳
     */
    String leaveRoom(SocketIOClient socketIOClient, RTCSwap rtcSwap) throws ClientException;
}
