import { Socket } from 'socket.io-client';
import { wsManager } from '@/composables/wsManager';

/*************************************以下为WebRTCSignaling命名空间逻辑****************************************/
export class WebRTCSignalingService {// 单例模式
    private static instance: WebRTCSignalingService;
    private webRTCSignalingSocket: Socket;
    private interval:NodeJS.Timeout|null = null;
    private constructor() {
        this.webRTCSignalingSocket = wsManager.socket("/DM", {
            auth: {
                token: localStorage.getItem("token") || ""
            }, retries: 3 // 最大重试次数。超过限制，数据包将被丢弃。
        });

        this.webRTCSignalingSocket.on('connect', () => {});

        this.webRTCSignalingSocket.on('connect_error', (error) => {
            console.error('Manager Connection error:', error);
            // 可以在这里处理重连逻辑
            if(this.interval) return;
            this.interval = setInterval(() => {
              this.connect(); // 重新连接
              this.interval && clearInterval(this.interval);
              this.interval=null;
            }, 5000); // 5秒后重试
        });

        this.webRTCSignalingSocket.on('reconnect_error', (error) => {
            console.error('Manager Reconnection error:', error);
        });
    
        this.webRTCSignalingSocket.on('disconnect', (reason) => {
            console.log('Manager Disconnected:', reason);
            if (reason === 'io server disconnect') {
                // 服务器主动断开连接，可以尝试重新连接
                setInterval(() => {
                this.connect();
                }, 5000); // 5秒后重试
            };
        });
    };

    public static getInstance(): WebRTCSignalingService {
        if (!WebRTCSignalingService.instance) {
            WebRTCSignalingService.instance = new WebRTCSignalingService();
        };

        return WebRTCSignalingService.instance;
    };

    public getSocket(): Socket {
        return this.webRTCSignalingSocket;
    };

    private connect() {
        this.webRTCSignalingSocket.connect();
    };

    public disconnect() {
        this.webRTCSignalingSocket.disconnect();
    };
};
/*************************************以上为WebRTCSignaling命名空间逻辑****************************************/