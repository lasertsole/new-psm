import { Manager, Socket } from 'socket.io-client';
import type { UserInfo } from '@/types/user';
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
  private interval:NodeJS.Timeout|null = null;

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
      if(this.interval) return;
      this.interval = setInterval(() => {
        this.connect(); // 重新连接
        this.interval&&clearInterval(this.interval);
        this.interval=null;
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


export const contactItems: Ref<ContactItem[]> = ref<ContactItem[]>([] as ContactItem[]);// 联系人列表
export const nowChatIndex: Ref<number> = ref(0);// 当前聊天窗口在联系人列表中的索引

/**
 * 跳转到私聊页面
 */
export function toDM(id:string, name:string, avatar:string):void {
  if(userInfo.id==id) {
    import.meta.client&&ElMessage.warning('不能私信自己');
    return;
  }
  
  let userIds: string[] = contactItems.value.length!=0?contactItems.value.map(user => user.id!):[];
  let index = userIds.indexOf(id);
  
  if(index!== -1){// 如果用户存在于联系人列表中
    // 将用户从原位置移除
    const [movedElement] = contactItems.value.splice(index, 1);
    // 将用户插入到列表头部
    contactItems.value.unshift(movedElement);
  }
  else{// 如果用户不存在于联系人列表中
    // 将用户插入到列表头部
    let newContactItem: ContactItem = {
      id,
      name,
      avatar,
      lastMessage: "",
      lastTime: "",
      unread: 0,
      isMuted: false,
      isGroup: false
    };

    contactItems.value.unshift(newContactItem);
  }
  
  nowChatIndex.value = 0;// 将当前聊天窗口设置为第一个
  
  navigateTo("/message");
};