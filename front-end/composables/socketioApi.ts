import { Manager, Socket } from 'socket.io-client';

let socketUrl:string = "ws://localhost:8001";

const manager: Manager= new Manager(socketUrl, {
  reconnection: false // 禁用自动重连
  ,transports: ['websocket'] // 默认是http轮训，设置使用websocket
  , upgrade: false // 关闭自动升级
});

export class OnetoOneChatService { // 单例模式
  private static instance: OnetoOneChatService;
  private socket: Socket;
  private constructor() {
    this.socket = manager.socket("/OneToOneChat", {
      auth: {
        token: localStorage.getItem("token") || ""
      }
      , retries: 3 // 最大重试次数。超过限制，数据包将被丢弃。
    });

    this.socket.on('connect', () => {
      console.log('Manager Connected to server');
    });

    this.socket.on('connect_error', (error) => {
      console.error('Manager Connection error:', error);
      // 可以在这里处理重连逻辑
      setInterval(() => {
        this.connect(); // 重新连接
      }, 5000); // 5秒后重试
    });

    this.socket.on('reconnect_error', (error) => {
      console.error('Manager Reconnection error:', error);
    });

    this.socket.on('disconnect', (reason) => {
      console.log('Manager Disconnected:', reason);
      if (reason === 'io server disconnect') {
        // 服务器主动断开连接，可以尝试重新连接
        setInterval(() => {
          this.connect();
        }, 5000); // 5秒后重试
      };
    });
  };

  public static getInstance(): OnetoOneChatService {
    if (!OnetoOneChatService.instance) {
      OnetoOneChatService.instance = new OnetoOneChatService();
    };

    return OnetoOneChatService.instance;
  };

  public getSocket(): Socket {
    return this.socket;
  };

  private connect() {
    this.socket.connect();
  };

  public disconnect() {
    this.socket.disconnect();
  };
};