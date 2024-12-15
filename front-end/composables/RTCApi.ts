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

    private videoDom: HTMLVideoElement | null = null;
    public inviteJoinArr: Reactive<RoomInvitation[]> = reactive<RoomInvitation[]>([]);// 短时间内的多条RTC邀请用列表缓存
    private ownRoom: Ref<Room | null> = ref<Room | null>(null);// 当前用户房间信息
    private userMediaConstraints: Reactive<MediaStreamConstraints> = reactive<MediaStreamConstraints>({ // 当前音视频媒体配置
        audio: true,
        video: true
    });
    private displayMediaConstraints: Reactive<DisplayMediaStreamOptions> = reactive<DisplayMediaStreamOptions>({ // 当前投屏媒体配置
      video: true,
      audio: false // 通常不包括音频
    });
    // 监听事件的钩子函数
    private inviteJoinRoomHooks: Array<Function> = [];
    private agreeJoinRoomHooks: Array<Function> = [];
    private rejectJoinRoomHooks: Array<Function> = [];
    private swapSDPHooks: Array<Function> = [];
    private swapCandidateHooks: Array<Function> = [];
    private leaveRoomHooks: Array<Function> = [];
    private trackBulidHooks: Array<Function> = [];

    // 添加监听邀请事件
    public onInviteJoinRoom(hook: Function) {
        this.inviteJoinRoomHooks.push(hook);
    };

    // 添加监听同意加入房间事件
    public onAgreeJoinRoom(hook: Function) {
        this.agreeJoinRoomHooks.push(hook);
    };

    // 添加监听拒绝加入房间事件
    public onRejectJoinRoom(hook: Function) {
        this.rejectJoinRoomHooks.push(hook);
    };

    // 添加监听交换SDP事件
    public onSwapSDP(hook: Function) {
        this.swapSDPHooks.push(hook);
    };

    // 添加监听交换候选事件
    public onSwapCandidate(hook: Function) {
        this.swapCandidateHooks.push(hook);
    };

    // 监听离开房间事件
    public onLeaveRoom(hook: Function) {
        this.leaveRoomHooks.push(hook);
    };

    // 监听轨道事件
    public onTrackBulid(hook: Function) {
        this.trackBulidHooks.push(hook);
    };

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

                this.RTCSocket.timeout(5000).emit('swapCandidate', candidateSwap);
            };
        };

        // 监听双方相互建立音频轨道事件
        peer.rtcPeerConnection.ontrack = (event: RTCTrackEvent) => {
            this.videoDom!.srcObject = event.streams[0];
            this.videoDom!.play();
            this.trackBulidHooks.forEach(hook => hook(event));
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
                this.RTCSocket.timeout(5000).emit("rejectJoinRoom", roomInvitation.roomId);
                return;
            } else if( this.ownRoom.value && this.ownRoom.value.roomId == roomInvitation.roomId ) {// 如果已经进入邀请函所指房间，说明是重复信息，则直接忽略
                return;
            };

            // 如果该邀请不存在邀请列表中，则将邀请放入邀请列表
            if(!this.inviteJoinArr.map(item => item.roomId).includes(roomInvitation.roomId)){
                this.inviteJoinArr.push(roomInvitation);
            };

            // 触发邀请加入房间成功钩子函数
            this.inviteJoinRoomHooks.forEach(hook => hook(roomInvitation));
        });

        // 监听同意加入房间事件
        this.RTCSocket.on("agreeJoinRoom", async (roomInvitation: RoomInvitation):Promise<void>=> {
            // 如果房间不存在，抛出错误
            if(roomInvitation.tgtUserName == userInfo.name){ // 如果是加入房间的是当前用户,则跳过处理
                return;
            };

            const room:Room = {
                roomId: roomInvitation.roomId!,
                roomOwnerId: roomInvitation.roomOwnerId!,
                roomName: roomInvitation.roomName,
                roomType: roomInvitation.roomType,
                peerMap: new Map<string, PeerOne>()
            };
            this.ownRoom.value = room;

            await this.initPeer(roomInvitation.tgtUserId);

            let peerOne: PeerOne = room.peerMap!.get(roomInvitation.tgtUserId)!;
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

            // 触发同意加入房间成功钩子函数
            this.agreeJoinRoomHooks.forEach(hook => hook(roomInvitation));

            this.RTCSocket.timeout(5000).emit('swapSDP', offerSDP);
        });

        // 监听拒绝加入房间事件
        this.RTCSocket.on("rejectJoinRoom", (roomInvitation: RoomInvitation):void=> {
            this.rejectJoinRoomHooks.forEach(hook => hook(roomInvitation));
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
            ) {
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

            // 执行swapSDP钩子函数
            this.swapSDPHooks.forEach(hook => hook(remoteSDP));

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

                this.RTCSocket.timeout(5000).emit('swapSDP', answerSDP);
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

            // 执行swapCandidate钩子函数
            this.swapCandidateHooks.forEach(hook => hook(remoteSDP));
        });

        // 监听离开房间事件
        this.RTCSocket.on("leaveRoom", (remoteSDP: RTCSwap): void=> {
            this.leaveRoomHooks.forEach(hook => hook(remoteSDP));
        });
    };

    // 创建房间
    public async createRoom(RTCWindowDom: HTMLVideoElement, outResolve?: Function, outReject?: Function):Promise<void> {
        await new Promise((resolve, reject)=> {
            const room: Room = {
                roomId: userInfo.id!,
                roomOwnerId: userInfo.id!,
                roomName: userInfo.name!+"'s room",
                roomType: RTCEnum.DRTC,
            };
    
            this.RTCSocket.timeout(5000).emit("createRoom", room, (err:any, isSuccussful:boolean):void=> { // 加入房间，房间号为用户id
                console.log("createRoom", err, isSuccussful);
                if(err) {
                    outReject&&outReject();// 调用失败回调函数
                    reject();
                    return;
                };
    
                room.peerMap = new Map<string, PeerOne>();
                this.ownRoom.value = room;
    
                outResolve&&outResolve();// 调用成功回调函数
                resolve(true);
            });
        });
    };

    // 邀请加入房间
    public async inviteJoinRoom(tgtUserId: string, tgtUserName: string, outResolve?: Function, outReject?: Function):Promise<void> {
        await new Promise((resolve, reject)=> {
            if(!this.ownRoom.value) {
                ElMessage.warning("please create room first.");
                reject();
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
    
            this.RTCSocket.timeout(5000).emit("inviteJoinRoom", roomInvitation, (err:any, timestamp:string):void=> { // 加入房间，房间号为用户id
                if(err){
                    outReject&&outReject(timestamp);// 调用失败回调函数
                    return;
                };
    
                outResolve&&outResolve(timestamp);
                resolve(true);
            });
        });
    };

    // 同意加入房间
    public async agreeJoinRoom(roomInvitation: RoomInvitation, outResolve?: Function, outReject?: Function):Promise<void> {
        await new Promise((resolve, reject)=>{
            this.RTCSocket.timeout(5000).emit("agreeJoinRoom", roomInvitation, (err:any, timestamp:string):void=> { // 加入房间，房间号为用户id
                if(err){
                    outReject&&outReject(timestamp);// 调用失败回调函数
                    reject();
                    return;
                };
    
                // 更新房间信息
                this.ownRoom.value = {
                    roomId: roomInvitation.roomId,
                    roomOwnerId: roomInvitation.roomOwnerId,
                    roomName: roomInvitation.roomName,
                    roomType: roomInvitation.roomType,
                    peerMap: new Map<string, PeerOne>(),
                };
    
                outResolve&&outResolve(timestamp);
    
                this.inviteJoinArr.splice(0, this.inviteJoinArr.length);// 清空列表

                resolve(true);
            });
        });
    };

    // 拒绝加入房间
    public async rejectJoinRoom(roomInvitation: RoomInvitation, resolve?: Function, reject?: Function):Promise<void> {
        await new Promise((resolve, reject)=>{
            this.RTCSocket.timeout(5000).emit("rejectJoinRoom", roomInvitation, (err:any, timestamp:string):void=> {// 加入房间，房间号为用户id
                if(err){
                    reject&&reject(timestamp);// 调用失败回调函数
                    return;
                };

                resolve&&resolve(timestamp);
            });

            // 将该邀请从邀请列表中删除
            let index:number = this.inviteJoinArr.map(item => item.roomId).indexOf(roomInvitation.roomId);
            this.inviteJoinArr.splice(index, 1);
        });
    };

    // 离开房间
    public async leaveRoom(resolve?: Function, reject?: Function):Promise<void> {
        await new Promise((resolve, reject)=>{
            this.ownRoom.value = null;
            this.RTCSocket.timeout(5000).emit("leaveRoom", (err:any, timestamp:string):void=> {// 加入房间，房间号为用户id
                if(err){
                    reject&&reject(timestamp);// 调用失败回调函数
                    return;
                };

                resolve&&resolve(timestamp);
            });
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