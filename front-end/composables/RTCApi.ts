import type { Reactive } from 'vue';
import { RTCEnum } from '@/enums/rtc.d';
import { Socket } from 'socket.io-client';
import type { RTCSwap } from "@/types/rtc";
import { wsManager } from '@/composables/wsManager';
import type { Room, RoomInvitation, PeerOne } from "@/types/common";

/*************************************以下为RTC命名空间逻辑****************************************/
export class RTCService {// 单例模式
    private static instance: RTCService | null;
    private RTCSocket: Socket;
    private interval: NodeJS.Timeout|null = null;

    private inviteJoinArr: RoomInvitation[] = [];// 短时间内的多条RTC邀请用列表缓存
    private ownRoom: Ref<Room | null> = ref<Room | null>(null);// 当前用户房间信息
    private userMediaConstraints: Reactive<MediaStreamConstraints> = reactive({ // 当前音视频媒体配置
        audio: true,
        video: true
    });
    private displayMediaConstraints: Reactive<DisplayMediaStreamOptions> = reactive({ // 当前投屏媒体配置
      video: true,
      audio: false // 通常不包括音频
    });

    // 获取本地音视频流
    private async getLocalStream():Promise<{userMediaStream:MediaStream, displayMediaStream:MediaStream}> {
        const userMediaStream:MediaStream = await navigator.mediaDevices.getUserMedia(this.userMediaConstraints);
        const displayMediaStream:MediaStream = await navigator.mediaDevices.getDisplayMedia(this.displayMediaConstraints);

        return { userMediaStream, displayMediaStream };
    };

    private async initPeer(userId: string):Promise<void> {
        if(!this.ownRoom.value) {
            throw new Error("当前房间不存在");
        } else if(this.ownRoom.value.peerMap!.has(userId)) return; // 如果已经存在，则不重复创建

        // 创建RTCPeerConnection实例
        const peer:PeerOne = {
            name: null,
            avatar: null,
            rtcPeerConnection: new RTCPeerConnection({
                iceServers: [
                    {"urls": "stun:stun.l.google.com:19302"},
                ]
            })
        };

        // peer加载本地媒体流
        const { userMediaStream, displayMediaStream } = await this.getLocalStream();

        // 添加用户媒体的轨道
        userMediaStream.getTracks().forEach(track => {
            peer.rtcPeerConnection.addTrack(track, userMediaStream);
        });

        // 添加屏幕共享的轨道
        displayMediaStream.getTracks().forEach(track => {
            peer.rtcPeerConnection.addTrack(track, displayMediaStream);
        });

        peer.rtcPeerConnection.onicecandidate = (event: RTCPeerConnectionIceEvent) => {
            if (event.candidate) { // 如果 candidate 不为空，将 candidate 缓存到本地变量中
                // 生成 SDP
                const candidateSwap: RTCSwap = {
                    roomId: this.ownRoom.value!.roomId,
                    srcUserId: userInfo.id!,
                    srcUserAvatar: userInfo.avatar!,
                    srcUserName: userInfo.name!,
                    tgtUserId: userId,
                    data: JSON.stringify(event.candidate)
                };

                this.RTCSocket.emit('swapCandidate', candidateSwap);
            };
        };

        this.ownRoom.value.peerMap!.set(userId, peer);
    };

    private constructor() {
        if(import.meta.server) throw new Error("RTCService can only be used in the browser.");

        this.RTCSocket = wsManager.socket("/RTC", {
            auth: {
                token: localStorage.getItem("token") || ""
            }, retries: 3 // 最大重试次数。超过限制，数据包将被丢弃。
        });

        this.RTCSocket.on('connect', () => {});

        this.RTCSocket.on('connect_error', (error) => {
            console.error('Manager Connection error:', error);
            // 可以在这里处理重连逻辑
            if(this.interval) return;
            this.interval = setInterval(() => {
              this.connect(); // 重新连接
              this.interval && clearInterval(this.interval);
              this.interval=null;
            }, 5000); // 5秒后重试
        });

        this.RTCSocket.on('reconnect_error', (error) => {
            console.error('Manager Reconnection error:', error);
        });
    
        this.RTCSocket.on('disconnect', (reason) => {
            console.log('Manager Disconnected:', reason);
            if (reason === 'io server disconnect') {
                // 服务器主动断开连接，可以尝试重新连接
                setInterval(() => {
                this.connect();
                }, 5000); // 5秒后重试
            };
        });

        // 监听邀请加入房间事件
        this.RTCSocket.on('inviteJoinRoom', (roomInvitation: RoomInvitation):void=> {
            let userIds: string[] = DMContactsItems.length!=0?DMContactsItems.map(user => user.tgtUserId!):[];
            if(!userIds.includes(roomInvitation.srcUserId)) {
                // 如果用户不存在于联系人列表中，则不是来着联系人的邀请,直接拒绝
                this.RTCSocket.emit("rejectJoinRoom", roomInvitation.roomId);
            };

            // 如果该邀请不存在邀请列表中，则将邀请放入邀请列表
            if(!this.inviteJoinArr.map(item => item.roomId).includes(roomInvitation.roomId)){
                this.inviteJoinArr.push(roomInvitation);
            };
        });

        // 监听同意加入房间事件
        this.RTCSocket.on("agreeJoinRoom", async (roomInvitation: RoomInvitation):Promise<void>=> {
            let room: Room | null = this.ownRoom.value;
            let peerOne: PeerOne | null = null;
            // 如果房间不存在，抛出错误
            if(!room){
                throw new Error("room is not exist");
            } else if(roomInvitation.tgtUserName == userInfo.name){ // 如果是加入房间的是当前用户,则跳过处理
                return;
            } else if(!room.peerMap!.get(roomInvitation.tgtUserId)) { // peer不存在时，创建并初始化peer
                await this.initPeer(roomInvitation.tgtUserId);
            };
            peerOne = room.peerMap!.get(roomInvitation.tgtUserId)!;
            peerOne.name = roomInvitation.tgtUserName;
            let rtcPeerConnection:RTCPeerConnection = peerOne!.rtcPeerConnection;

            // 生成 Offer
            const localOffer = await rtcPeerConnection.createOffer({
                offerToReceiveAudio: true,
                offerToReceiveVideo: true
            });

            // 设置本地描述
            await rtcPeerConnection.setLocalDescription(localOffer);

            const offerSDP: RTCSwap = {
                roomId: room.roomId!,
                srcUserId: userInfo.id!,
                srcUserName: userInfo.name!,
                srcUserAvatar: userInfo.avatar!,
                tgtUserId: roomInvitation.tgtUserId,
                data: JSON.stringify(localOffer)
            };

            this.RTCSocket.emit('swapSDP', offerSDP);
        });

        // 监听拒绝加入房间事件
        this.RTCSocket.on("rejectJoinRoom", (roomInvitation: RoomInvitation):void=> {
            ElMessage.warning(`${roomInvitation.srcUserName} rejected your invitation to join the room.`);
        });


        // 监听交换SDP事件
        this.RTCSocket.on("swapSDP", async (remoteSDP: RTCSwap):Promise<void>=> {
            let room: Room | null = this.ownRoom.value;
            let peerOne: PeerOne | null = null;
            if(!remoteSDP || !remoteSDP.data) {
                ElMessage.warning("Received invalid SDP.");
                return;
            } else if(!room) {
                ElMessage.warning("room is not exists.");
                return;
            } else if(remoteSDP.roomId!=this.ownRoom.value!.roomId// 如果房间ID不匹配，则忽略
                || remoteSDP.srcUserId==userInfo.id// 如果发送该SDP信息的就是本人，则忽略
                || remoteSDP.tgtUserId!=userInfo.id// 如果该SDP的接收对象不是本人，则忽略
            ){
                return;
            } else if(!room.peerMap!.get(remoteSDP.srcUserId)){ // peer连接不存在时，创建并初始化peer连接
                await this.initPeer(remoteSDP.srcUserId);
            };

            peerOne = room.peerMap!.get(remoteSDP.srcUserId)!;
            peerOne.name = remoteSDP.srcUserName; // 设置对方用户名
            peerOne.avatar = remoteSDP.srcUserAvatar;// 设置对方用户头像

            let rtcPeerConnection:RTCPeerConnection = peerOne!.rtcPeerConnection;

            // 设置远程描述
            await rtcPeerConnection.setRemoteDescription(JSON.parse(remoteSDP.data));

            if(rtcPeerConnection.localDescription) {// 如果没有发过SDP，则发送Answer
                // 生成 Answer
                const answer: RTCSessionDescriptionInit = await rtcPeerConnection.createAnswer();
                await rtcPeerConnection.setLocalDescription(answer);

                // 生成 SDP
                const answerSDP: RTCSwap = {
                    roomId: this.ownRoom.value!.roomId,
                    srcUserId: userInfo.id!,
                    srcUserName: userInfo.name!,
                    srcUserAvatar: userInfo.avatar!,
                    tgtUserId: remoteSDP.srcUserId,
                    data: JSON.stringify(answer)
                };

                this.RTCSocket.emit('swapSDP', answerSDP);
            };
        });

        // 监听交换候选者事件
        this.RTCSocket.on("swapCandidate", async (remoteSDP: RTCSwap):Promise<void>=> {
            let room: Room | null = this.ownRoom.value;
            let peerOne: PeerOne | null = null;
            if(!remoteSDP || !remoteSDP.data) {
                ElMessage.warning("Received invalid SDP.");
                return;
            } else if(!room) {
                ElMessage.warning("room is not exists.");
                return;
            } else if(remoteSDP.roomId!=this.ownRoom.value!.roomId// 如果房间ID不匹配，则忽略
                || remoteSDP.srcUserId==userInfo.id// 如果发送该SDP信息的就是本人，则忽略
                || remoteSDP.tgtUserId!=userInfo.id// 如果该SDP的接收对象不是本人，则忽略
            ){
                return;
            } else if(!room.peerMap!.get(remoteSDP.srcUserId)){ // peer连接不存在时，创建并初始化peer连接
                await this.initPeer(remoteSDP.srcUserId);
            };

            peerOne = room.peerMap!.get(remoteSDP.srcUserId)!;

            let rtcPeerConnection:RTCPeerConnection = peerOne!.rtcPeerConnection;

            rtcPeerConnection.addIceCandidate(JSON.parse(remoteSDP.data));
        });

        // 监听离开房间事件
        this.RTCSocket.on("leaveRoom", (timestamp: string): void=> {
            console.log("timestamp", timestamp);
        });
    };

    // 创建房间
    public createRoom(resolve?: Function, reject?: Function):void {
        const room: Room = {
            roomId: userInfo.id!,
            roomOwnerId: userInfo.id!,
            roomName: userInfo.name!,
            roomType: RTCEnum.DRTC,
        };

        this.RTCSocket.emit("createRoom", room, (isSuccussful:boolean):void=> {// 加入房间，房间号为用户id
            if(!isSuccussful) {
                reject&&reject();// 调用失败回调函数
                return;
            };

            room.peerMap = new Map<string, PeerOne>();
            this.ownRoom.value = room;

            resolve&&resolve();// 调用成功回调函数
        });
    };

    // 邀请加入房间
    public inviteJoinRoom(tgtUserId: string, tgtUserName: string, resolve?: Function, reject?: Function):void {
        if(!this.ownRoom.value) {
            ElMessage.warning("请先创建房间");
            return;
        };

        const roomInvitation: RoomInvitation = {
            roomId: this.ownRoom.value.roomId,
            roomOwnerId: this.ownRoom.value.roomOwnerId,
            roomName: this.ownRoom.value.roomName,
            roomType: this.ownRoom.value.roomType,
            srcUserId: userInfo.id!,
            srcUserName: userInfo.name!,
            tgtUserId,
            tgtUserName
        };

        this.RTCSocket.emit("inviteJoinRoom", roomInvitation, (timestamp:string):void=> {// 加入房间，房间号为用户id
            if(!isTimestamp(timestamp)){
                reject&&reject(timestamp);// 调用失败回调函数
                return;
            };

            resolve&&resolve(timestamp);
        });
    };

    // 同意加入房间
    public async agreeJoinRoom(roomInvitation: RoomInvitation, resolve?: Function, reject?: Function):Promise<void> {
        this.RTCSocket.emit("agreeJoinRoom", roomInvitation, (timestamp:string)=> {// 加入房间，房间号为用户id
            if(!isTimestamp(timestamp)){
                reject&&reject(timestamp);// 调用失败回调函数
                return;
            };

            resolve&&resolve(timestamp);
        });
        
        this.inviteJoinArr=[];// 清空列表

        // 更新房间信息
        this.ownRoom.value = {
            roomId: roomInvitation.roomId,
            roomOwnerId: roomInvitation.roomOwnerId,
            roomName: roomInvitation.roomName,
            roomType: roomInvitation.roomType,
            peerMap: new Map<string, PeerOne>(),
        };
    };

    // 拒绝加入房间
    public rejectJoinRoom(roomInvitation: RoomInvitation, resolve?: Function, reject?: Function):void {
        this.RTCSocket.emit("rejectJoinRoom", roomInvitation, (timestamp:string):void=> {// 加入房间，房间号为用户id
            if(!isTimestamp(timestamp)){
                reject&&reject(timestamp);// 调用失败回调函数
                return;
            };

            resolve&&resolve(timestamp);
        });

        // 将该邀请从邀请列表中删除
        let index:number = this.inviteJoinArr.map(item => item.roomId).indexOf(roomInvitation.roomId);
        this.inviteJoinArr.splice(index, 1);
    };

    // 离开房间
    public leaveRoom(resolve?: Function, reject?: Function): void {
        this.ownRoom.value = null;
        this.RTCSocket.emit("leaveRoom", (timestamp:string):void=> {// 加入房间，房间号为用户id
            if(!isTimestamp(timestamp)){
                reject&&reject(timestamp);// 调用失败回调函数
                return;
            };

            resolve&&resolve(timestamp);
        });
    };

    // 获取RTC服务单例
    public static getInstance(): RTCService {
        if (!RTCService.instance) {
            RTCService.instance = new RTCService();
        };

        return RTCService.instance;
    };

    // 销毁RTC服务单例
    public static destroyInstance() {
        if (RTCService.instance) {
            RTCService.instance.disconnect();
            RTCService.instance = null;
        };
    };

    public getSocket():Socket {
        return this.RTCSocket;
    };

    private connect() {
        this.RTCSocket.connect();
    };

    public disconnect() {
        this.RTCSocket.disconnect();
    };
};
/*************************************以上为RTC命名空间逻辑****************************************/