import { Manager, Socket } from 'socket.io-client';
import type { ContactItem, MessageItem, Sender } from '@/types/socketIO';

let socketUrl:string = "ws://localhost:8001";

const manager: Manager= new Manager(socketUrl, {
  reconnection: false // 禁用自动重连
  ,transports: ['websocket'] // 默认是http轮训，设置使用websocket
  , upgrade: false // 关闭自动升级
});

export class DMService { // 单例模式
  private static instance: DMService;
  private socket: Socket;
  public contactItem: ContactItem[] = [] as ContactItem[];

  private constructor() {
    this.socket = manager.socket("/DM", {
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

  public static getInstance(): DMService {
    if (!DMService.instance) {
      DMService.instance = new DMService();
    };

    return DMService.instance;
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

export function toDM(id:string):void {
  if(userInfo.id==id) {
    import.meta.client&&ElMessage.warning('不能私信自己');
    return;
  }

  navigateTo("/message?userId="+id+"&type=dm");
};

export function quicklyChat(userId:string, type:string) {
  const dmService = DMService.getInstance();
  let userIds: string[] = dmService.contactItem.length !== 0
    ? dmService.contactItem
        .filter(item => item.id !== undefined) // 过滤掉 id 为 undefined 的项
        .map(item => item.id!)
    : [];
  let index = userIds.indexOf(userId);
  if(index!== -1){
    // 将元素从原位置移除
    const [movedElement] = dmService.contactItem.splice(index, 1);
    // 将元素插入到列表头部
    dmService.contactItem.unshift(movedElement);
    
    return true;
  }
  else{
    return false;
  }
}