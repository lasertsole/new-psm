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
    type?: 'text';
    content?: string;
    senderId?: string;
    receiverId?: string;
    time?: string;
    isRead?: boolean;
    isUrgent?: boolean;
    isDeleted?: boolean;
};