import type { Reactive } from 'vue';
import type { Page } from "@/types/common";
import type { UserInfo } from "@/types/user";
import { Manager, Socket } from 'socket.io-client';
import { fromEvent, concatAll, concatMap, scan, map, filter, tap } from "rxjs"; 
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
export const isInitDM:Ref<boolean> = ref<boolean>(false);// 是否已初始化

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
    type InitStatus = { processArr:Page<MessageItem>[], unProcessArr:Page<MessageItem>[], count:number, minIndex:number };
    const initStatus:InitStatus = {processArr:[], unProcessArr:[], count: 0, minIndex: 1};
    // 使用rxjs确保接收的历史聊天记录是有序的
    fromEvent(this.DMSocket, 'initMessage').pipe(
      scan((status:any, curPage: Page<MessageItem>) => {
        status.total = curPage.total!;
        if(curPage.current!<=status.minIndex) {
          status.processArr.push(curPage);
          status.minIndex++;
          while (status.unProcessArr.indexOf(status.minIndex)>=0) {
            status.processArr.push(status.minIndex);
            status.unProcessArr.splice(status.unProcessArr.indexOf(status.minIndex), 1);
            status.minIndex++;
          }
        }
        else{
          status.unProcessArr.push(curPage);
        };
        status.count++;
        return status;
      }, initStatus)
      , filter((status:any)=>status.processArr.length!=0)
      , map(item=>{return item.processArr}),
      tap((value:any)=>{
        initStatus.processArr = [];
      })
      , concatAll()
      ,concatMap(async (messageObjPage: Page<MessageItem>):Promise<Page<MessageItem> | undefined> => {
        // 确保返回的是有效数据
        if(!messageObjPage||messageObjPage.records?.length==0) return undefined;
        let messageObjs:MessageItem[] = messageObjPage.records!;
        // 找出服务器返回的信息中的所有联系人并绑定相关聊天记录，放在Map(key为userId,value为ContactsItem)里,每个联系人只放一次
        const contactsMap:Map<string, Reactive<ContactsItem>> = new Map();
        messageObjs.forEach((messageObj)=>{
          
          let contactItem:Reactive<ContactsItem> = reactive<ContactsItem>({
            tgtUserId: "", // tgtUserId暂时不确定
            srcUserId: userInfo.id!,
            lastMessage: messageObj.content!,
            lastTime: messageObj.timestamp,
            messageItems: []
          });

          let modifyId:string;
          if(messageObj.srcUserId==userInfo.id) {
            modifyId=messageObj.tgtUserId!;
          } else {
            modifyId=messageObj.srcUserId!;
          };
          if(!contactsMap.has(modifyId)) {
            contactsMap.set(modifyId, contactItem);
          };
          contactsMap.get(modifyId)!.tgtUserId = modifyId;// 确定tgtUserId
          contactsMap.get(modifyId)!.messageItems.push(messageObj);
          contactsMap.get(modifyId)!.lastMessage = messageObj.content!;
          contactsMap.get(modifyId)!.lastTime = messageObj.timestamp;

          //将信息放进indexedDB里
          db.MessageDBItems.add({
            ...messageObj,
            isDeleted: false,
            status: 'sent',
            type: 'text',
            maxUserId: max( messageObj.srcUserId!, messageObj.tgtUserId! ), 
            minUserId: min( messageObj.srcUserId!, messageObj.tgtUserId! ),
          } as MessageDBItem);
        });

        // 筛选出新的联系人
        contactsItems.forEach((item)=>{
          // 如果有，则先更新联系人列表聊天记录和indexedDB，再把联系人从contactsMap中删除
          if(contactsMap.has(item.tgtUserId)) {
            //更新页面左侧显示的联系人列表
            item.lastMessage = contactsMap.get(item.tgtUserId)!.lastMessage;
            item.lastTime = contactsMap.get(item.tgtUserId)!.lastTime;
            item.messageItems = item.messageItems.concat(contactsMap.get(item.tgtUserId)!.messageItems);

            //更新indexedDB里的联系人列表
            db.ContactsDBItems.where('tgtUserId').equals(item.tgtUserId).modify({
              lastMessage: item.lastMessage,
              lastTime: item.lastTime
            });

            // 把联系人从contactsMap中删除
            contactsMap.delete(item.tgtUserId)
          };
        });
        let newContacts:Reactive<ContactsItem>[] = [...contactsMap.values()];

        // 如果没有新的联系人，则直接退出
        if(newContacts.length==0) return;

        // 请求联系人信息
        let userInfos:UserInfo[] = await getUserByIds(newContacts.map(item=>item.tgtUserId));
        // 将新的联系人插入联系人列表里和indexedDB里
        userInfos.forEach((user)=> {
          let contactsItem = contactsMap.get(user.id!);
          contactsItems.push({
            tgtUserId:contactsItem!.tgtUserId!,
            srcUserId:contactsItem!.srcUserId!,
            name: user.name,
            avatar: user.avatar,
            lastMessage: contactsItem!.lastMessage,
            lastTime: contactsItem!.lastTime,
            unread: 0,
            isMuted: false,
            isGroup: false,
            messageItems: contactsItem!.messageItems
          });

          db.ContactsDBItems.add({
            name: user.name,
            avatar: user.avatar,
            lastMessage: contactsItem!.lastMessage,
            lastTime: contactsItem!.lastTime,
            unread: 0,
            isMuted: false,
            isGroup: false,
            tgtUserId: contactsItem!.tgtUserId,// tgtUserId为登录用户的联系人Id
            srcUserId: contactsItem!.srcUserId// srcUserId为登录用户Id
          });
        });
        return messageObjPage;
      })
    ).subscribe((page:Page<MessageItem> | undefined)=>{
      if(page==undefined||page.current==page.total) {// 如果当前页是最后一页，则说明所有消息都接收完毕，初始化状态置为true
        if(contactsItems.length==0) { // 如果没有联系人，则说明所有消息都接收完毕，初始化状态置为true
          // DM初始化状态置为true
          isInitDM.value = true;
          return;
        }

        // 按照时间顺序对左边列表进行排序
        let sortArr:Reactive<ContactsItem>[];
        let startIndex:number = 0;
        if(nowDMContactsIndex.value==0) {// nowDMContactsIndex如果不为-1，说明已执行toDM函数，则排除索引下表为0的元素
          startIndex=1;
        }
        // 将所有联系人列表复制一份用于排序
        sortArr=contactsItems.slice(startIndex);

        // 排序,按时间降序
        sortArr.sort((a,b)=>{
          return b.lastTime!.localeCompare(a.lastTime!);
        });

        // 将排序后的数组赋值给contactsItems
        contactsItems.concat(sortArr);

        // DM初始化状态置为true
        isInitDM.value = true;
      }
    });

    // 监听接收到的消息，使用rxjs确保消息被按序处理,不会存在并发或插队现象
    fromEvent(this.DMSocket, "receiveMessage").pipe(concatMap(async (messageItem:MessageItem):Promise<void> =>{
      // 通过自旋堵塞确保本函数是在初始化完成后才执行，否则会导致消息丢失
      while(!isInitDM.value) {await new Promise(resolve => setTimeout(resolve, 500));};

      let userIds: string[] = contactsItems.length!=0?contactsItems.map(user => user.tgtUserId!):[];
      let index = userIds.indexOf(messageItem.srcUserId!);

      if(index!== -1) {// 如果用户存在于联系人列表中，则更新该用户的信息
        contactsItems[index].messageItems!.push(messageItem);
        contactsItems[index].lastMessage = messageItem.content!;
        contactsItems[index].lastTime = messageItem.timestamp;
        
        //更新indexedDB里的联系人列表
        db.ContactsDBItems.where('tgtUserId').equals(messageItem.srcUserId!).modify({
          lastMessage: messageItem.content,
          lastTime: messageItem.timestamp
        });

        // 将聊天记录插入到indexedDB
        db.MessageDBItems.add({
          ...messageItem,
          isDeleted: false,
          status: 'sent',
          maxUserId: max( messageItem.srcUserId!, messageItem.tgtUserId! ), 
          minUserId: min( messageItem.srcUserId!, messageItem.tgtUserId! ),
        });
      } else {// 如果用户不存在于联系人列表中
        // 占位的同时获取用户信息
        let srcUserInfo: UserInfo | null = await getUserById(messageItem.srcUserId!);
        if(!srcUserInfo){ // 如果是虚假用户，则直接返回
          return;
        }
        else{
          // 构建联系人对象
          let newContactItem: ContactsDBItem = {
            tgtUserId: messageItem.srcUserId!,
            srcUserId: userInfo.id!,
            lastMessage: messageItem.content!,
            lastTime: messageItem.timestamp,
            name: srcUserInfo.name,
            avatar: srcUserInfo.avatar,
            unread: 0,
            isMuted: false,
            isGroup: false,
          };

          // 将新用户信息插入到contactsItems
          contactsItems.unshift(reactive({
            ...newContactItem,
            messageItems: [reactive(messageItem)]
          }) as Reactive<ContactsItem>);

          //将新用户信息插入到indexedDB
          db.ContactsDBItems.add(newContactItem);

          // 将聊天记录插入到indexedDB
          db.MessageDBItems.add({
            ...messageItem,
            isDeleted: false,
            status: 'sent',
            maxUserId: max( messageItem.srcUserId!, messageItem.tgtUserId! ), 
            minUserId: min( messageItem.srcUserId!, messageItem.tgtUserId! ),
          });
        };
      }
    })).subscribe((x:any)=>{});
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

  nowDMContactsIndex.value = 0;// 将当前聊天窗口设置为第一个

  // 初始化联系人和信息
  nextTick(async ()=>{
    await initDM();
  });
  
  // 通过自旋堵塞确保本函数是在初始化完成后才执行，否则会导致消息丢失
  while(!isInitDM.value) {await new Promise(resolve => setTimeout(resolve, 500));};

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
  
  navigateTo("/message");
};

// 初始化(获取联系人列表和信息)
export async function initDM(): Promise<void> {
  // 如果已初始化过，直接退出
  if(!userInfo.isLogin || isInitDM.value) return;
  
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
    maxLastTime = maxDate(maxLastTime, new Date(contactDBItem.lastTime!+"z"));// 一定要加z，告诉JS，这个时间戳是UTC国际时间戳

    // 返回 ContactsItem类型数据
    contactsItems.push({
      ...contactDBItem,
      messageItems: [] as MessageItem[] // 消息列表
    });
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
export async function sendMessage(message:string): Promise<void> {
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