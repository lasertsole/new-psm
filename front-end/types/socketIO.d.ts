export type ContactItem = {
    id?: string;
    name?: string;
    avatar?: string;
    lastMessage?: string;
    lastTime?: string;
    unread?: number;
    isOnline?: boolean;
    isMuted?: boolean;
    isFollowed?: boolean;
    isIdle?: boolean;
    owner?: string;
    isGroup?: boolean;
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
    type?: string;
    content?: string;
    sender?: string;
    receiver?: string;
    time?: string;
    isRead?: boolean;
    isMine?: boolean;
    isUrgent?: boolean;
    isDeleted?: boolean;
};