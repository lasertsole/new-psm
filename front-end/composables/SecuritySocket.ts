import { Socket } from 'socket.io-client';

export class SecurityService {
    private static instance: SecurityService | null;
    private SecuritySocket: Socket;
    private interval: NodeJS.Timeout|null = null;

    private constructor() {
        if(import.meta.server) throw new Error("SecurityService can only be used in the browser.");

        this.SecuritySocket = wsManager.socket("/security", {
            auth: {
                token: localStorage.getItem("token") || ""
            }, retries: 3 // 最大重试次数。超过限制，数据包将被丢弃。
        });

        this.SecuritySocket.on('connect', () => {});

        this.SecuritySocket.on('connect_error', (error: any) => {
            console.error('Manager Connection error:', error);
            // 可以在这里处理重连逻辑
            if(this.interval) return;
            this.interval = setInterval(() => {
              this.connect(); // 重新连接
              this.interval && clearInterval(this.interval);
              this.interval=null;
            }, 5000); // 5秒后重试
        });

        this.SecuritySocket.on('reconnect_error', (error: any) => {
            console.error('Manager Reconnection error:', error);
        });
    
        this.SecuritySocket.on('disconnect', (reason: Socket.DisconnectReason) => {
            console.log('Manager Disconnected:', reason);
            if (reason === 'io server disconnect') {
                // 服务器主动断开连接，可以尝试重新连接
                setInterval(() => {
                this.connect();
                }, 5000); // 5秒后重试
            };
        });

        this.SecuritySocket.on('otherLogin', (ip: string, callback) => {
            console.log("ip", ip);
            console.log("callback", callback);
            // // 弹出警告
            // ElNotification({
            //     title: 'warning',
            //     message: "Another device has logged into your account, and you have been forcibly logged out.",
            //     type: 'warning',
            // });

            // // 登出
            // forcedLogout();
            // callback(true);
        });
    };

    // 获取RTC服务单例
    public static getInstance(): SecurityService {
        if (!SecurityService.instance) {
            SecurityService.instance = new SecurityService();
        };

        return SecurityService.instance;
    };

    // 销毁Security服务单例
    public static destroyInstance() {
        if (SecurityService.instance) {
            SecurityService.instance.disconnect();
            SecurityService.instance = null;
        };
    };

    public getSocket():Socket {
        return this.SecuritySocket;
    };

    private connect() {
        this.SecuritySocket.connect();
    };

    public disconnect() {
        this.SecuritySocket.disconnect();
    };
};