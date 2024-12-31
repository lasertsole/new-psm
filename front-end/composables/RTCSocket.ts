import type { Reactive } from 'vue';
import { RTCEnum } from '@/enums/rtc.d';
import { Socket } from 'socket.io-client';
import type { RTCSwap } from "@/types/rtc";
import { fromEvent, concatMap } from "rxjs"; 
import { wsManager } from '@/composables/wsManager';
import { DMContactsItems } from "@/composables/DMSocket";
import type { Room, RoomInvitation, PeerOne } from "@/types/common";

/*************************************以下为RTC命名空间逻辑****************************************/
export class RTCService {// 单例模式
    private static instance: RTCService | null;
    private RTCSocket: Socket;
    private interval: NodeJS.Timeout|null = null;

    private mainVideoDom: HTMLVideoElement | null = null;
    private subVideoDom: HTMLVideoElement | null = null;
    public inviteJoinArr: Reactive<RoomInvitation[]> = reactive<RoomInvitation[]>([]);// 短时间内的多条RTC邀请用列表缓存
    private ownRoom: Ref<Room | null> = ref<Room | null>(null);// 当前用户房间信息
    private userMediaConstraints: Reactive<MediaStreamConstraints> = reactive<MediaStreamConstraints>({ // 当前音视频媒体配置
        audio: true,
        video: true
    });
    private displayMediaConstraints: Reactive<DisplayMediaStreamOptions> = reactive<DisplayMediaStreamOptions>({ // 当前投屏媒体配置
      video: true,
      audio: true
    });
    private userMediaStream: MediaStream | null = null;// 本地音视频流
    private displayMediaStream: MediaStream | null = null;// 本地投屏媒体流

    // 监听事件的钩子函数
    private inviteJoinRoomHooks: Array<(roomInvitation:RoomInvitation)=>void> = [];
    private agreeJoinRoomHooks: Array<(roomInvitation:RoomInvitation)=>void> = [];
    private rejectJoinRoomHooks: Array<(roomInvitation: RoomInvitation)=>void> = [];
    private swapSDPHooks: Array<(remoteSDP: RTCSwap)=>void> = [];
    private swapCandidateHooks: Array<(remoteSDP: RTCSwap)=>void> = [];
    private leaveRoomHooks: Array<(remoteSDP: RTCSwap)=>void> = [];
    private trackBulidHooks: Array<(event: RTCTrackEvent)=>void> = [];

    // 添加监听邀请事件
    public onInviteJoinRoom(hook: (roomInvitation: RoomInvitation)=>void) {
        this.inviteJoinRoomHooks.push(hook);
    };

    // 添加监听同意加入房间事件
    public onAgreeJoinRoom(hook: (roomInvitation:RoomInvitation)=>void) {
        this.agreeJoinRoomHooks.push(hook);
    };

    // 添加监听拒绝加入房间事件
    public onRejectJoinRoom(hook: (roomInvitation: RoomInvitation)=>void) {
        this.rejectJoinRoomHooks.push(hook);
    };

    // 添加监听交换SDP事件
    public onSwapSDP(hook: (remoteSDP: RTCSwap)=>void) {
        this.swapSDPHooks.push(hook);
    };

    // 添加监听交换候选事件
    public onSwapCandidate(hook: (remoteSDP: RTCSwap)=>void) {
        this.swapCandidateHooks.push(hook);
    };

    // 监听离开房间事件
    public onLeaveRoom(hook: (remoteSDP: RTCSwap)=>void) {
        this.leaveRoomHooks.push(hook);
    };

    // 监听轨道事件
    public onTrackBulid(hook: (event: RTCTrackEvent)=>void) {
        this.trackBulidHooks.push(hook);
    };

    // 初始化视频dom元素
    public initVideoDom(mainVideoDom: HTMLVideoElement, subVideoDom: HTMLVideoElement) {
        this.mainVideoDom = mainVideoDom;
        this.subVideoDom = subVideoDom;
    };

    // 获取本地音视频流
    public async getLocalStream():Promise<{userMediaStream:MediaStream, displayMediaStream:MediaStream}> {
        if(!this.userMediaStream) {this.userMediaStream = await navigator.mediaDevices.getUserMedia(this.userMediaConstraints);};
        if(!this.displayMediaStream) {this.displayMediaStream = await navigator.mediaDevices.getDisplayMedia(this.displayMediaConstraints);};

        return { userMediaStream: this.userMediaStream, displayMediaStream:this.displayMediaStream };
    };

    private async initPeer(userId: string):Promise<void> {
        if(!this.ownRoom.value) {
            throw new Error("当前房间不存在");
        } else if(!this.mainVideoDom) {
            throw new Error("未赋值视频dom元素");
        } else if(this.ownRoom.value.peerMap!.has(userId)) return; // 如果已经存在，则不重复创建

        // 创建RTCPeerConnection实例
        const peer:PeerOne = {
            name: null,
            avatar: null,
            rtcPeerConnection: new RTCPeerConnection({
                iceServers: [
                    {"urls": "stun:stun.l.google.com:19302"},
                ]
            }),
            hasRemoteSDP: false,
            hasLocalSDP: false,
            hasRemoteCandidate: false,
            hasLocalCandidate: false
        };

        // peer加载本地媒体流
        const { userMediaStream, displayMediaStream } = await this.getLocalStream();

        // 将用户媒体流绑定到次要视频元素上
        this.subVideoDom!.srcObject = displayMediaStream;
        this.subVideoDom!.play();

        // 添加用户媒体的轨道
        userMediaStream.getTracks().forEach(track => {
            peer.rtcPeerConnection.addTrack(track, userMediaStream);
        });

        // 添加屏幕共享的轨道
        displayMediaStream.getTracks().forEach(track => {
            peer.rtcPeerConnection.addTrack(track, displayMediaStream);
        });

        peer.rtcPeerConnection.onicecandidate = async(event: RTCPeerConnectionIceEvent) => {
            // 通过自旋堵塞确保本函数是在得到远端SDP后才执行，否则会导致顺序紊乱
            let lockTimes = 0;
            let timeWait = 500;
            while(!peer.hasRemoteSDP) {
                await new Promise(resolve => setTimeout(resolve, timeWait));
                // 如果自旋超过60秒，则认为发生错误，清空该连接
                lockTimes++;
                if(lockTimes*timeWait/1000 > 60) {
                    ElMessage.warning("RTC连接失败");
                    this.ownRoom.value?.peerMap!.delete(userId);
                };
            };

            if (event.candidate && !peer.hasLocalCandidate) { // 如果 candidate 不为空，且本地hasLocalCandidate没有设置，则将 candidate 缓存到本地变量中
                peer.hasLocalCandidate = true;// 标记本地已经缓存ICE candidate
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
            if(this.mainVideoDom!.srcObject) return; // 如果视频dom元素已经绑定了流，则不重复绑定

            this.mainVideoDom!.srcObject = event.streams[0];// 将对方轨道绑定到主要视频dom元素
            this.mainVideoDom!.play();
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

        this.RTCSocket.on('connect_error', (error: any) => {
            console.error('Manager Connection error:', error);
            // 可以在这里处理重连逻辑
            if(this.interval) return;
            this.interval = setInterval(() => {
              this.connect(); // 重新连接
              this.interval && clearInterval(this.interval);
              this.interval=null;
            }, 5000); // 5秒后重试
        });

        this.RTCSocket.on('reconnect_error', (error: any) => {
            console.error('Manager Reconnection error:', error);
        });
    
        this.RTCSocket.on('disconnect', (reason: Socket.DisconnectReason) => {
            console.log('Manager Disconnected:', reason);
            if (reason === 'io server disconnect') {
                // 服务器主动断开连接，可以尝试重新连接
                setInterval(() => {
                this.connect();
                }, 5000); // 5秒后重试
            };
        });

        // 监听邀请加入房间事件
        fromEvent(this.RTCSocket, 'inviteJoinRoom').pipe(concatMap(async (roomInvitation: RoomInvitation):Promise<boolean>=> {
            let userIds: string[] = DMContactsItems.length!=0?DMContactsItems.map(user => user.tgtUserId!):[];
            if(!this.mainVideoDom) {
                throw new Error("未赋值视频dom元素");
            } else if(!userIds.includes(roomInvitation.srcUserId)) {
                // 如果用户不存在于联系人列表中，则不是来着联系人的邀请,直接拒绝
                this.RTCSocket.timeout(5000).emit("rejectJoinRoom", roomInvitation.roomId);
                return false;
            } else if( this.ownRoom.value && this.ownRoom.value.roomId == roomInvitation.roomId ) {// 如果已经进入邀请函所指房间，说明是重复信息，则直接忽略
                return false;
            };

            // 如果该邀请不存在邀请列表中，则将邀请放入邀请列表
            if(!this.inviteJoinArr.map(item => item.roomId).includes(roomInvitation.roomId)){
                this.inviteJoinArr.push(roomInvitation);
            };

            // 触发邀请加入房间成功钩子函数
            this.inviteJoinRoomHooks.forEach(hook => hook(roomInvitation));
            return true;
        })).subscribe((res:boolean)=>{});

        // 监听同意加入房间事件
        fromEvent(this.RTCSocket, "agreeJoinRoom").pipe(concatMap(async (roomInvitation: RoomInvitation):Promise<boolean>=> {
            if(!this.mainVideoDom) {
                throw new Error("未赋值视频dom元素");
            } //如果还没有房间,或者房间号与已加入的房间号不同，则创建房间对象
            else if(!this.ownRoom.value || this.ownRoom.value.roomId != roomInvitation.roomId){
                // 创建房间对象
                this.ownRoom.value = {
                    roomId: roomInvitation.roomId!,
                    roomOwnerId: roomInvitation.roomOwnerId!,
                    roomName: roomInvitation.roomName,
                    roomType: roomInvitation.roomType,
                    peerMap: new Map<string, PeerOne>()
                };
            };

            await this.initPeer(roomInvitation.tgtUserId);

            let peerOne: PeerOne = this.ownRoom.value.peerMap!.get(roomInvitation.tgtUserId)!;
            peerOne.name = roomInvitation.tgtUserName;
            let rtcPeerConnection:RTCPeerConnection = peerOne!.rtcPeerConnection;

            // 生成 Offer
            const localOffer = await rtcPeerConnection.createOffer({
                offerToReceiveAudio: true,
                offerToReceiveVideo: true
            });

            // 设置本地描述
            await rtcPeerConnection.setLocalDescription(localOffer);
            peerOne.hasLocalSDP = true;// 标记本地已经缓存SDP

            const offerSDP: RTCSwap = {
                roomId: this.ownRoom.value.roomId!,
                srcUserId: userInfo.id!,
                srcUserName: userInfo.name!,
                srcUserAvatar: userInfo.avatar!,
                tgtUserId: roomInvitation.tgtUserId,
                data: JSON.stringify(localOffer)
            };

            // 触发同意加入房间成功钩子函数
            this.agreeJoinRoomHooks.forEach(hook => hook(roomInvitation));

            this.RTCSocket.timeout(5000).emit('swapSDP', offerSDP);
            return true;
        })).subscribe((res:Promise<boolean>)=>{});

        // 监听拒绝加入房间事件
        fromEvent(this.RTCSocket, "rejectJoinRoom").pipe(concatMap(async (roomInvitation: RoomInvitation):Promise<boolean>=> {
            this.rejectJoinRoomHooks.forEach(hook => hook(roomInvitation));
            return true;
        })).subscribe((res:boolean)=>{});

        // 监听交换SDP事件
        fromEvent(this.RTCSocket, "swapSDP").pipe(concatMap(async (remoteSDP: RTCSwap):Promise<boolean>=> {
            let room: Room | null = this.ownRoom.value;
            let peerOne: PeerOne | null = null;
            if(!this.mainVideoDom) {
                throw new Error("未赋值视频dom元素");
            } else if(!remoteSDP || !remoteSDP.data) {
                ElMessage.warning("Received invalid SDP.");
                return false;
            } else if(!room) {
                ElMessage.warning("room is not exists.");
                return false;
            } else if(remoteSDP.roomId!=this.ownRoom.value!.roomId// 如果房间ID不匹配，则忽略
                || remoteSDP.srcUserId==userInfo.id// 如果发送该SDP信息的就是本人，则忽略
                || remoteSDP.tgtUserId!=userInfo.id// 如果该SDP的接收对象不是本人，则忽略
            ) {
                return false;
            } else if(!room.peerMap!.get(remoteSDP.srcUserId)){ // peer连接不存在时，创建并初始化peer连接
                await this.initPeer(remoteSDP.srcUserId);
            };

            peerOne = room.peerMap!.get(remoteSDP.srcUserId)!;

            if(peerOne.hasRemoteSDP) return true;// 如果已经缓存了SDP，则忽略

            peerOne.name = remoteSDP.srcUserName; // 设置对方用户名
            peerOne.avatar = remoteSDP.srcUserAvatar;// 设置对方用户头像

            let rtcPeerConnection:RTCPeerConnection = peerOne!.rtcPeerConnection;

            // 设置远程描述
            await rtcPeerConnection.setRemoteDescription(JSON.parse(remoteSDP.data));
            peerOne.hasRemoteSDP = true;// 标记远程已经缓存SDP

            // 执行swapSDP钩子函数
            this.swapSDPHooks.forEach(hook => hook(remoteSDP));

            if(!peerOne.hasLocalSDP) {// 如果没有缓存过SDP,说明没发过SDP，则发送Answer
                // 生成 Answer
                const answer: RTCSessionDescriptionInit = await rtcPeerConnection.createAnswer();
                await rtcPeerConnection.setLocalDescription(answer);
                peerOne.hasLocalSDP = true;// 标记本地已经缓存SDP

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
            return true;
        })).subscribe((res:Promise<boolean>)=>{});

        // 监听交换候选者事件
        fromEvent(this.RTCSocket, "swapCandidate").pipe(concatMap(async (remoteSDP: RTCSwap):Promise<boolean>=> {
            let room: Room | null = this.ownRoom.value;
            let peerOne: PeerOne | null = null;
            if(!this.mainVideoDom) {
                throw new Error("未赋值视频dom元素");
            } else if(!remoteSDP || !remoteSDP.data) {
                ElMessage.warning("Received invalid SDP.");
                return false;
            } else if(!room) {
                ElMessage.warning("room is not exists.");
                return false;
            } else if(remoteSDP.roomId!=this.ownRoom.value!.roomId// 如果房间ID不匹配，则忽略
                || remoteSDP.srcUserId==userInfo.id// 如果发送该SDP信息的就是本人，则忽略
                || remoteSDP.tgtUserId!=userInfo.id// 如果该SDP的接收对象不是本人，则忽略
            ) {
                return false;
            } else if(!room.peerMap!.get(remoteSDP.srcUserId)){ // peer连接不存在时，创建并初始化peer连接
                await this.initPeer(remoteSDP.srcUserId);
            };

            peerOne = room.peerMap!.get(remoteSDP.srcUserId)!;

            if(peerOne.hasRemoteCandidate) return true;// 如果已经缓存了SDP，则忽略

            let rtcPeerConnection:RTCPeerConnection = peerOne!.rtcPeerConnection;

            rtcPeerConnection.addIceCandidate(JSON.parse(remoteSDP.data));
            peerOne.hasRemoteCandidate = true;// 标记远程已经缓存候选者

            // 执行swapCandidate钩子函数
            this.swapCandidateHooks.forEach(hook => hook(remoteSDP));
            return true;
        })).subscribe((res:Promise<boolean>)=>{});

        // 监听离开房间事件
        fromEvent(this.RTCSocket, "leaveRoom").pipe(concatMap(async (remoteSDP: RTCSwap):Promise<boolean>=> {
            // 清空连接
            this.ownRoom.value!.peerMap!.delete(remoteSDP.srcUserId);

            // 执行离开房间钩子函数
            this.leaveRoomHooks.forEach(hook => hook(remoteSDP));
            return true;
        })).subscribe((res:boolean)=>{});
    };

    // 创建房间
    public async createRoom(outResolve?: (isSuccussful:boolean)=>void, outReject?: ()=>void):Promise<void> {
        if(!this.mainVideoDom) {
            throw new Error("未赋值视频dom元素");
        };

        await new Promise((resolve, reject)=> {
            const room: Room = {
                roomId: userInfo.id!,
                roomOwnerId: userInfo.id!,
                roomName: userInfo.name!+"'s room",
                roomType: RTCEnum.DRTC,
            };
    
            this.RTCSocket.timeout(5000).emit("createRoom", room, (err:any, isSuccussful:boolean):void=> { // 加入房间，房间号为用户id
                if(err) {
                    outReject&&outReject();// 调用失败回调函数
                    reject();
                };
    
                room.peerMap = new Map<string, PeerOne>();
                this.ownRoom.value = room;
    
                outResolve&&outResolve(isSuccussful);// 调用成功回调函数
                resolve(true);
            });
        });
    };

    // 邀请加入房间
    public async inviteJoinRoom(tgtUserId: string, tgtUserName: string, outResolve?: (timestamp:string)=>void, outReject?: ()=>void):Promise<void> {
        if(!this.mainVideoDom) {
            throw new Error("未赋值视频dom元素");
        };
        
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
                    outReject&&outReject();// 调用失败回调函数
                    reject();
                };
    
                outResolve&&outResolve(timestamp);
                resolve(true);
            });
        });
    };

    // 同意加入房间
    public async agreeJoinRoom(roomInvitation: RoomInvitation, outResolve?: (timestamp:string)=>void, outReject?: ()=>void):Promise<void> {
        if(!this.mainVideoDom) {
            throw new Error("未赋值视频dom元素");
        };
        
        await new Promise((resolve, reject)=>{
            this.RTCSocket.timeout(5000).emit("agreeJoinRoom", roomInvitation, (err:any, timestamp:string):void=> { // 加入房间，房间号为用户id
                if(err){
                    outReject&&outReject();// 调用失败回调函数
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
    
                this.inviteJoinArr.splice(0, this.inviteJoinArr.length);// 清空列表
                outResolve&&outResolve(timestamp); // 调用成功回调函数
                resolve(true);
            });
        });
    };

    // 拒绝加入房间
    public async rejectJoinRoom(roomInvitation: RoomInvitation, outResolve?: (timestamp:string)=>void, outReject?: ()=>void):Promise<void> {
        await new Promise((subResolve, subReject)=>{
            this.RTCSocket.timeout(5000).emit("rejectJoinRoom", roomInvitation, (err:any, timestamp:string):void=> {// 加入房间，房间号为用户id
                if(err){
                    outReject&&outReject();// 调用失败回调函数
                    subReject();
                };

                // 将该邀请从邀请列表中删除
                let index:number = this.inviteJoinArr.map(item => item.roomId).indexOf(roomInvitation.roomId);
                this.inviteJoinArr.splice(index, 1);

                outResolve&&outResolve(timestamp);
                subResolve(true);
            });
        });
    };

    // 离开房间
    public async leaveRoom(outResolve?: (timestamp:string)=>void, outReject?: ()=>void):Promise<void> {
        await new Promise((resolve, reject)=>{
            this.ownRoom.value = null;
            this.RTCSocket.timeout(5000).emit("leaveRoom", (err:any, timestamp:string):void=> {// 加入房间，房间号为用户id
                if(err){
                    outReject&&outReject();// 调用失败回调函数
                    reject();
                };

                outResolve&&outResolve(timestamp);
                resolve(true);
            });
        });
    };

    // 获取RTC服务单例
    public static async getInstance(): Promise<RTCService> {
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