import type { Reactive } from 'vue';
import { Manager, Socket } from 'socket.io-client';
import type { ContactsItem, MessageItem, MessageDBItem, ContactsDBItem } from '@/types/chat';

let socketUrl:string = "ws://localhost:8001";

const manager: Manager= new Manager(socketUrl, {
  reconnection: false // 禁用自动重连
  ,transports: ['websocket'] // 默认是http轮训，设置使用websocket
  , upgrade: false // 关闭自动升级
});


/*************************************以下为DM命名空间逻辑****************************************/
export const contactsItems: Reactive<ContactsItem>[] = reactive([] as ContactsItem[]);// 联系人列表
export const nowDMContactsIndex: Ref<number> = ref(-1);// 当前聊天窗口在联系人列表中的索引
let isInitDM:boolean = false;// 是否已初始化

type DMConfig = {
  DMExpireDay: number
}

export class DMService { // 单例模式
  private static instance: DMService;
  private DMSocket: Socket;
  private interval:NodeJS.Timeout|null = null;
  private config: DMConfig = { // 配置项
    DMExpireDay: 7, // 默认7天
  };
  private constructor() {
    this.DMSocket = manager.socket("/DM", {
      auth: {
        token: localStorage.getItem("token") || ""
      }, retries: 3 // 最大重试次数。超过限制，数据包将被丢弃。
    });
    
    this.DMSocket.on('connect', () => {});

    this.DMSocket.on('connect_error', (error) => {
      console.error('Manager Connection error:', error);
      // 可以在这里处理重连逻辑
      if(this.interval) return;
      this.interval = setInterval(() => {
        this.connect(); // 重新连接
        this.interval && clearInterval(this.interval);
        this.interval=null;
      }, 5000); // 5秒后重试
    });

    this.DMSocket.on('reconnect_error', (error) => {
      console.error('Manager Reconnection error:', error);
    });

    this.DMSocket.on('disconnect', (reason) => {
      console.log('Manager Disconnected:', reason);
      if (reason === 'io server disconnect') {
        // 服务器主动断开连接，可以尝试重新连接
        setInterval(() => {
          this.connect();
        }, 5000); // 5秒后重试
      };
    });

    /**
     * 初始化DMConfig
     */
    this.DMSocket.on("initDMConfig", (item: DMConfig) => {
      this.config = item;
    });

    /**
     * 从服务器接收历史信息记录
     */
    // this.DMSocket.on("initMessage", (messageObj: MessageItem[]) => {
    //   // 找出服务器返回的信息中的所有联系人并绑定相关聊天记录，放在Map(key为userId,value为ContactsItem)里,每个联系人只放一次
    //   const contactsMap:Map<string, Reactive<contactItem>> = new Map();
    //   messageObj.forEach((item)=>{
        
    //     let contactItem:Reactive<contactItem> = reactive<ContactsItem>({
    //       tgtUserId: item.tgtUserId,
    //       srcUserId: userInfo.id!,
    //       lastMessage: item.content,
    //       lastTime: item.timestamp,
    //       messageItems: []
    //     });

    //     if(item.srcUserId==userInfo.id) {

    //       if(!contactsMap.has(item.tgtUserId)) {
    //         contactsMap.set(item.tgtUserId, contactItem);
    //       };
    //       contactsMap.get(item.tgtUserId).messageItems.push(item);
    //     } else {

    //       if(!contactsMap.has(item.srcUserId)) {
    //         contactsMap.set(item.srcUserId, contactItem);
    //       };
    //       contactsMap.get(item.srcUserId).messageItems.push(item);
    //     };

    //     //将信息放进indexedDB里
    //     db.MessageDBItems.add({
    //       ...messageObj,
    //       isDeleted: false,
    //       status: 'pending',
    //       type: 'text',
    //       maxUserId: max( messageObj.srcUserId!, messageObj.tgtUserId! ), 
    //       minUserId: min( messageObj.srcUserId!, messageObj.tgtUserId! ),
    //     });
    //   });
      
    //   // 筛选出新的联系人
    //   contactsItems.forEach((item)=>{
    //     if(contactsMap.has(item.tgtUserId)) contactsMap.delete(item.tgtUserId);
    //   });
    //   let newContacts:Reactive<ContactsItem>[] = [...contactsMap.values()];

    //   // 将新的联系人插入联系人列表里
    //   newContacts.forEach((item)=>{
    //     contactsItems.push(item);
    //   });
    // });

    // this.DMSocket.on("receiveMessage", (MessageItem: MessageItem)=>{
    //   console.log(MessageItem);
    //   let userIds: string[] = contactsItems.value.length!=0?contactsItems.value.map(user => user.id!):[];
    //   let index = userIds.indexOf(MessageItem.srcUserId!);

    //   if(index!== -1){
    //     contactsItems.value[index].MessageItems!.push(MessageItem)
    //   }
    //   else{// 如果用户不存在于联系人列表中,则向服务器请求该联系人个人信息，得到头像和名字后再添加到联系列表
        
    //   }
    // });
  };

  public static getInstance(): DMService {
    if (!DMService.instance) {
      DMService.instance = new DMService();
    };

    return DMService.instance;
  };

  public getSocket(): Socket {
    return this.DMSocket;
  };

  private connect() {
    this.DMSocket.connect();
  };

  public disconnect() {
    this.DMSocket.disconnect();
  };
};

/**
 * 跳转到私聊页面
 */
export async function toDM(tgtUserId:string, name:string, avatar:string): Promise<void> {
  
  if(userInfo.id==tgtUserId) {
    import.meta.client && ElMessage.warning('不能私信自己');
    return;
  }

  // 初始化联系人和信息
  await initDM();

  let tgtUserIds: string[] = contactsItems.length!=0?contactsItems.map(user => user.tgtUserId!):[];
  let index = tgtUserIds.indexOf(tgtUserId);
  
  if(index!== -1) { // 如果用户存在于联系人列表中
    // 将用户从原位置移除
    const [movedElement] = contactsItems.splice(index, 1);
    // 将用户插入到列表头部
    contactsItems.unshift(movedElement);
  }
  else {// 如果用户不存在于联系人列表中
    // 将用户插入到列表头部
    let newContactItem: Reactive<ContactsItem> = reactive<ContactsItem>({
      tgtUserId,
      srcUserId: userInfo.id!,
      name,
      avatar,
      lastMessage: "",
      lastTime: "",
      unread: 0,
      isMuted: false,
      isGroup: false,
      messageItems: []
    });

    contactsItems.unshift(newContactItem);
  };
  
  nowDMContactsIndex.value = 0;// 将当前聊天窗口设置为第一个
  
  navigateTo("/message");
};

// 初始化(获取联系人列表和信息)
export async function initDM(): Promise<void> {
  // 如果已初始化过，直接退出
  if(!userInfo.isLogin || isInitDM) return;
  isInitDM = true;
  
  // 从本地indexedDB拿去最新联系人列表
  let contactsDBItems: ContactsDBItem[] = await db.ContactsDBItems
  .where('srcUserId')
  .equals(userInfo.id!)
  .sortBy('timestamp');
  
  // 最晚的信息的时间戳(默认是UTC国际时间戳，来着服务端)
  let maxLastTime: Date = new Date(0);

  // 将每个 ContactsDBItem 转换为 ContactsItem
  contactsDBItems.forEach(contactDBItem => {
    // 筛选出最大的时间戳
    maxLastTime = maxDate(maxLastTime, new Date(contactDBItem.lastTime!));
    // console.log(contactDBItem);

    // 返回 ContactsItem类型数据
    contactsItems.push({
      ...contactDBItem,
      messageItems: [] as MessageItem[] // 消息列表
    } );
  });

  // 从本地indexedDB拿取最新聊天信息
  contactsItems.forEach(async item => {
    let messageDBItems: MessageDBItem[] = await db.MessageDBItems
    .where('[maxUserId+minUserId]')
    .equals([max(item.tgtUserId, item.srcUserId), min(item.tgtUserId, item.srcUserId)])
    .sortBy('timestamp');

    messageDBItems.map((item)=>{
      let { maxUserId, minUserId, ...messageItem } = item;
      return messageItem;
    });
    
    item.messageItems = messageDBItems as Reactive<MessageItem>[];
  });
  
  // 从服务器获取最新聊天信息
  let socket: Socket = DMService.getInstance().getSocket();
  socket.timeout(5000).emit('initMessage', maxLastTime);
};

// 发送信息逻辑
export function sendMessage(message:string): void {
  // 创建一个联系人对象
  let constactsObj:ContactsItem = contactsItems[nowDMContactsIndex.value];
  
  // 创建一个消息对象
  let messageObj:MessageItem = {
    type: 'text',
    content: message,
    srcUserId: userInfo.id,
    tgtUserId: constactsObj.tgtUserId,
    isDeleted: false,
    status: 'pending'
  };
  
  // 根据消息对象，创建一个响应式消息对象
  const messageItem: Reactive<MessageItem> = reactive<MessageItem>(messageObj);
  
  // 将消息对象添加到消息列表
  constactsObj.messageItems!.push(messageItem);
  
  // 生成发送信息时客户端的时间戳（UTC国际通用）,精确到微秒级别
  const formattedTimestamp = getUTCTimeNow();

  messageItem.timestamp = formattedTimestamp;
  
  // 发送消息
  let socket: Socket = DMService.getInstance().getSocket();
  socket.timeout(5000).emit('sendMessage', messageItem, (err:any, res:any)=> {
    // 如果有错误，则显示错误信息状态
    if (err) {
      messageItem.status = 'error';
      return;
    };

    // 更新消息状态
    messageItem.status = 'sent';

    // 根据服务器返回的时间戳,更新消息时间
    messageItem.timestamp = res;

    // 若该联系人在indexedDB数据库的联系人列表，则更新该联系人的最近联系时间，否则插入该联系人的记录
    let {messageItems, ...contactsDBItem} = constactsObj;
    contactsDBItem.lastTime = res;
    contactsDBItem.lastMessage = message;
    contactsDBItem = {
      ...contactsDBItem,
      tgtUserId: messageObj.tgtUserId,
      srcUserId: messageObj.srcUserId,
    } as ContactsDBItem;
    db.ContactsDBItems.put(contactsDBItem);
    
    // 将消息对象添加到indexedDB数据库
    let messageDBItem: MessageDBItem = {
      ...messageObj,
      maxUserId: max( messageObj.srcUserId!, messageObj.tgtUserId! ), 
      minUserId: min( messageObj.srcUserId!, messageObj.tgtUserId! ),
    };
    db.MessageDBItems.add(messageDBItem);

    // 更新左侧联系人列表
    constactsObj.lastTime = res;
    constactsObj.lastMessage = message;
  });
};

/*************************************以上为DM命名空间逻辑****************************************/

/*************************************以下为非命名空间逻辑****************************************/
/*************************************以上为根命名空间逻辑****************************************/