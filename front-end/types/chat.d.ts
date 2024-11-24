import type { Reactive } from "vue";

export type ContactsItem = {
    id?: string;
    tgtUserId: string;// tgtUserId为登录用户的联系人Id
    srcUserId: string;// srcUserId为登录用户Id
    name?: string;
    avatar?: string;
    lastMessage: string;
    lastTime?: string; //最后一次联系时间
    unread?: number;
    isMuted?: boolean;
    isGroup?: boolean; //是否是群聊
    messageItems: Reactive<MessageItem>[];// 消息列表
};

export type ContactsDBItem = Omit<ContactsItem, 'messageItems'>;

export type Contacts = {
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
    timestamp?: string;
    type?: 'text';
    content?: string;
    isRead?: boolean;
    isUrgent?: boolean;
    isDeleted?: boolean;
    status?: 'pending' | 'sent' | 'error' | 'read';
};

export type MessageDBItem = MessageItem & {
    maxUserId?: string;// 用于加速索引的字段,是tgtUserId和srcUserId中最大的那个字段
    minUserId?: string;// 用于加速索引的字段,是tgtUserId和srcUserId中最小的那个字段
};