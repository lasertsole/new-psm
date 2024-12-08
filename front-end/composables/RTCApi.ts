import { Socket } from 'socket.io-client';
import type { Room, RoomInvitation } from "@/types/common";
import { wsManager } from '@/composables/wsManager';

/*************************************以下为RTC命名空间逻辑****************************************/
export class RTCService {// 单例模式
    private static instance: RTCService | null;
    private RTCSocket: Socket;
    private interval: NodeJS.Timeout|null = null;
    private constructor() {
        if(import.meta.server) throw new Error("RTCService can only be used in the browser.");

        this.RTCSocket = wsManager.socket("/RTC", {
            auth: {
                token: localStorage.getItem("token") || ""
            }, retries: 3 // 最大重试次数。超过限制，数据包将被丢弃。
        });

        this.RTCSocket.on('connect', () => {
            this.RTCSocket.emit("createRoom", userInfo.id, (err:any, isCreateSuccessful:boolean)=> {// 加入房间，房间号为用户id
                if(err) return;

                console.log("isCreateSuccessful", isCreateSuccessful);
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

        this.RTCSocket.on('receiveRoomInvitation', (roomInvitation: RoomInvitation) => {
            console.error('roomInvitation', roomInvitation);
        });
    };

    public inviteJoinRoom(tarUserId: string):void {
        this.RTCSocket.emit("inviteJoinRoom", tarUserId, (err:any, timestamp:string)=> {// 加入房间，房间号为用户id
            if(err) return;

            console.log("timestamp", timestamp);
        });
    }

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