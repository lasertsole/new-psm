import { Socket } from 'socket.io-client';
import type { Room, RoomInvitation } from "@/types/common";
import { wsManager } from '@/composables/wsManager';

/*************************************以下为RTC命名空间逻辑****************************************/
export class RTCService {// 单例模式
    private static instance: RTCService | null;
    private RTCSocket: Socket;
    private interval: NodeJS.Timeout|null = null;
    private inviteJoinArr: RoomInvitation[] = [];// 短时间内的多条RTC邀请用列表缓存
    private hasOwnerRoom: boolean = false;

    private constructor() {
        if(import.meta.server) throw new Error("RTCService can only be used in the browser.");

        this.RTCSocket = wsManager.socket("/RTC", {
            auth: {
                token: localStorage.getItem("token") || ""
            }, retries: 3 // 最大重试次数。超过限制，数据包将被丢弃。
        });

        this.RTCSocket.on('connect', () => {
            this.RTCSocket.emit("createRoom", userInfo.id, (isCreateSuccessful:boolean)=> {// 加入房间，房间号为用户id

                console.log("isCreateSuccessful", isCreateSuccessful);
                this.hasOwnerRoom = isCreateSuccessful;
            });
        });

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

        this.RTCSocket.on('inviteJoinRoom', (roomInvitation: RoomInvitation):void=> {
            let userIds: string[] = contactsItems.length!=0?contactsItems.map(user => user.tgtUserId!):[];
            if(!userIds.includes(roomInvitation.srcUserId)) {
                // 如果用户不存在于联系人列表中，则不是来着联系人的邀请,直接拒绝
                this.RTCSocket.emit("rejectJoinRoom", roomInvitation.roomId);
            };
            this.inviteJoinArr.push(roomInvitation);
        });

        this.RTCSocket.on("agreeJoinRoom", (timestamp: string):void=> {
            console.log("timestamp", timestamp);
        });

        this.RTCSocket.on("rejectJoinRoom", (timestamp: string):void=> {
            console.log("timestamp", timestamp);
        });

        this.RTCSocket.on("swapSDP", (timestamp: string): void=> {
            console.log("timestamp", timestamp);
        });

        this.RTCSocket.on("swapCandidate", (timestamp: string): void=> {
            console.log("timestamp", timestamp);
        });

        this.RTCSocket.on("leaveRoom", (timestamp: string): void=> {
            console.log("timestamp", timestamp);
        });
    };

    public inviteJoinRoom(tarUserId: string):void {
        this.RTCSocket.emit("inviteJoinRoom", tarUserId, (err:any, timestamp:string)=> {// 加入房间，房间号为用户id
            console.log("timestamp", timestamp);
        });
    };

    public agreeJoinRoom(roomId: string):void {
        this.RTCSocket.emit("agreeJoinRoom", roomId, (err:any, timestamp:string)=> {// 加入房间，房间号为用户id
            if(err) return;

            console.log("timestamp", timestamp);
        });
        this.inviteJoinArr=[];// 清空列表
    };

    public rejectJoinRoom(roomId: string):void {
        this.RTCSocket.emit("rejectJoinRoom", roomId);
        this.inviteJoinArr=[];// 清空列表
    };

    public swapSDP(): void {
        this.RTCSocket.emit("swapSDP");
    };

    public swapCandidate(): void {
        this.RTCSocket.emit("swapCandidate");
    };

    public leaveRoom(): void {
        this.RTCSocket.emit("leaveRoom");
    };

    public static getInstance(): RTCService {
        if (!RTCService.instance) {
            RTCService.instance = new RTCService();
        };

        return RTCService.instance;
    };

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