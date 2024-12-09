import { Socket } from 'socket.io-client';
import { RTCEnum } from '@/enums/rtc';
import type { RTCSwap } from "@/types/rtc";
import { wsManager } from '@/composables/wsManager';
import type { Room, RoomInvitation } from "@/types/common";

/*************************************以下为RTC命名空间逻辑****************************************/
export class RTCService {// 单例模式
    private static instance: RTCService | null;
    private RTCSocket: Socket;
    private interval: NodeJS.Timeout|null = null;

    private localOffer: RTCSessionDescriptionInit | null = null;// 本地的RTC描述信息
    private localCandidate: RTCIceCandidateInit | null = null;// 本地的RTC候选信息
    private inviteJoinArr: RoomInvitation[] = [];// 短时间内的多条RTC邀请用列表缓存
    private ownRoom: Ref<Room | null> = ref<Room | null>(null);// 当前用户房间信息
    private peer: RTCPeerConnection | null = null;// RTC连接实例
    private banSendSDP: boolean = false; // 是否禁止发送SDP，当已发送SDP后，不再发送。但在新用户加入房间后，需要重新发送SDP，便会解封
    private banSendCandidate: boolean = false; // 是否禁止发送Candidate，当已发送Candidate后，不再发送。但新用户加入房间后，需要重新发送Candidate，便会解封

    // 获取本地音视频流
    private async getLocalStream():Promise<{userMediaStream:MediaStream, displayMediaStream:MediaStream}> {
        const userMediaConstraints = {
            video: true,
            audio: true
        };
        const userMediaStream:MediaStream = await navigator.mediaDevices.getUserMedia(userMediaConstraints);
        
        const displayMediaConstraints = {
            video: true,
            audio: false // 通常不包括音频
        };
        const displayMediaStream:MediaStream = await navigator.mediaDevices.getDisplayMedia(displayMediaConstraints);
        
        if (this.peer) {
            // 添加用户媒体的轨道
            userMediaStream.getTracks().forEach(track => {
                this.peer!.addTrack(track, userMediaStream);
            });

            // 添加屏幕共享的轨道
            displayMediaStream.getTracks().forEach(track => {
                this.peer!.addTrack(track, displayMediaStream);
            });
        };

        return { userMediaStream, displayMediaStream };
    };

    private async initPeer():Promise<void> {
        // 创建RTCPeerConnection实例
        this.peer = new RTCPeerConnection({
            iceServers: [
                {"urls": "stun:stun.l.google.com:19302"},
            ]
        });

        // peer加载本地媒体流
        await this.getLocalStream();

        // 生成 Offer
        this.localOffer = await this.peer.createOffer({
            offerToReceiveAudio: true,
            offerToReceiveVideo: true
        });
        // 设置本地描述
        await this.peer.setLocalDescription(this.localOffer);

        this.peer.onicecandidate = (event: RTCPeerConnectionIceEvent) => {
            if (event.candidate) { // 如果 candidate 不为空，将 candidate 缓存到本地变量中
                this.localCandidate = event.candidate;
            };
        };
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
            let userIds: string[] = contactsItems.length!=0?contactsItems.map(user => user.tgtUserId!):[];
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
            // peer连接不存在时，创建并初始化peer连接
            if(!this.peer) {
                await this.initPeer();
            };

            const SDP: RTCSwap = {
                roomId: userInfo.id!,
                srcUserId: userInfo.id!,
                srcUserName: userInfo.name!,
                data: JSON.stringify(this.localOffer)
            };

            this.RTCSocket.emit('swapSDP', SDP);
        });

        // 监听拒绝加入房间事件
        this.RTCSocket.on("rejectJoinRoom", (roomInvitation: RoomInvitation):void=> {
            ElMessage.warning(`${roomInvitation.srcUserName} rejected your invitation to join the room.`);
        });


        // 监听交换SDP事件
        this.RTCSocket.on("swapSDP", async (remoteSDP: RTCSwap):Promise<void>=> {
            if(!remoteSDP||!remoteSDP.data) {
                ElMessage.warning("Received invalid SDP.");
                this.ownRoom.value=null;
                return;
            } else if(!this.peer) {
                ElMessage.warning("RTC connection not ready.");
                this.ownRoom.value=null;
                return;
            };

            // // 解析远端 SDP
            // const remoteDescription: RTCSessionDescriptionInit = {
            //     type: "offer", // 假设收到的是 Offer
            //     sdp: JSON.parse(remoteSDP.data)
            // };

            // // 设置远程描述
            // await this.peer.setRemoteDescription(remoteDescription);

            if(!this.banSendSDP) {// 如果没有禁用SDP，则发送SDP
                // 生成 Answer
                const answer: RTCSessionDescriptionInit = await this.peer.createAnswer();
                await this.peer.setLocalDescription(answer);

                // 生成 SDP
                const ownSDP: RTCSwap = {
                    roomId: this.ownRoom.value!.roomId,
                    srcUserId: userInfo.id!,
                    srcUserName: userInfo.name!,
                    data: JSON.stringify(answer)
                };

                this.RTCSocket.emit('swapSDP', ownSDP);
                this.banSendSDP = true;
            };
        });

        // 监听交换候选者事件
        this.RTCSocket.on("swapCandidate", (timestamp: string): void=> {
            console.log("timestamp", timestamp);
        });

        // 监听离开房间事件
        this.RTCSocket.on("leaveRoom", (timestamp: string): void=> {
            console.log("timestamp", timestamp);
        });
    };

    // 创建房间
    public createRoom():void {
        const room: Room = {
            roomId: userInfo.id!,
            roomOwnerId: userInfo.id!,
            roomName: userInfo.name!,
            roomType: RTCEnum.DRTC,
        };

        this.RTCSocket.emit("createRoom", room, (isSuccussful:boolean):void=> {// 加入房间，房间号为用户id
            if(!isSuccussful) {
                ElMessage.warning("创建房间失败");
                return;
            };

            room.memberIdSet = new Set([userInfo.id!]);
            this.ownRoom.value = room;
        });
    };

    // 邀请加入房间
    public inviteJoinRoom(tarUserId: string, tarUserName: string):void {
        if(!this.ownRoom.value) {
            ElMessage.warning("请先创建房间");
            return;
        } else if(!this.peer) {
            ElMessage.warning("RTC connection not ready.");
            this.ownRoom.value=null;
            return;
        };

        const roomInvitation: RoomInvitation = {
            roomId: this.ownRoom.value.roomId,
            roomOwnerId: this.ownRoom.value.roomOwnerId,
            roomName: this.ownRoom.value.roomName,
            roomType: this.ownRoom.value.roomType,
            srcUserId: userInfo.id!,
            srcUserName: userInfo.name!,
            tarUserId,
            tarUserName
        };

        this.RTCSocket.emit("inviteJoinRoom", roomInvitation, (timestamp:string):void=> {// 加入房间，房间号为用户id
            console.log("timestamp", timestamp);
        });
    };

    // 同意加入房间
    public async agreeJoinRoom(roomInvitation: RoomInvitation):Promise<void> {
        // peer连接不存在时，创建并初始化peer连接
        if(!this.peer) {
            await this.initPeer();
        };

        this.RTCSocket.emit("agreeJoinRoom", roomInvitation, (timestamp:string)=> {// 加入房间，房间号为用户id
            console.log("timestamp", timestamp);
        });
        
        this.inviteJoinArr=[];// 清空列表

        // 更新房间信息
        this.ownRoom.value = {
            roomId: roomInvitation.roomId,
            roomOwnerId: roomInvitation.roomOwnerId,
            roomName: roomInvitation.roomName,
            roomType: roomInvitation.roomType,
        }
    };

    // 拒绝加入房间
    public rejectJoinRoom(roomInvitation: RoomInvitation):void {
        this.RTCSocket.emit("rejectJoinRoom", roomInvitation);

        // 将该邀请从邀请列表中删除
        let index:number = this.inviteJoinArr.map(item => item.roomId).indexOf(roomInvitation.roomId);
        this.inviteJoinArr.splice(index, 1);
    };

    // 交换SDP
    public swapSDP(): void {
        if(!this.ownRoom.value) {
            ElMessage.warning("请先创建房间");
            return;
        } else if(!this.peer) {
            ElMessage.warning("RTC connection not ready.");
            this.ownRoom.value=null;
            return;
        };

        if(!this.banSendSDP) {// 如果没有禁用SDP，则发送SDP
            const ownSDP: RTCSwap = {
                roomId: userInfo.id!,
                srcUserId: userInfo.id!,
                srcUserName: userInfo.name!,
                data: JSON.stringify(this.localOffer)
            };
            this.RTCSocket.emit('swapSDP', ownSDP);
            this.banSendSDP = true;
        };
    };

    // 离开房间
    public leaveRoom(): void {
        this.RTCSocket.emit("leaveRoom");
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