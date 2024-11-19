export type ContactItem = {
    id?: string;
    name?: string;
    avatar?: string;
    lastMessage?: string;
    lastTime?: string;
    unread?: number;
    isMuted?: boolean;
    isGroup?: boolean; //是否是群聊
    MessageItems: MessageItem[];// 消息列表
};

export type Sender = {
    id?: string;
    name?: string;
    avatar?: string;
    isOnline?: boolean;
    isFollowed?: boolean;
    isIdle?: boolean;
};

export type MessageItem = {
    id?: string;
    tgtUserId?: string;
    srcUserId?: string;
    type?: 'text';
    content?: string;
    time?: string;
    isRead?: boolean;
    isUrgent?: boolean;
    isDeleted?: boolean;
    status?: 'pending' | 'sent' | 'error' | 'read';
    request?: (MessageItems: MessageItem[], this) => Promise<any>;//异步发送函数与回调函数
};