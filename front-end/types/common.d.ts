export type Page<T> = {
    current?: number;
    size?: number;
    total?: number;
    records?: T[];
    pages?: number;
    nextAfterKeys?: string[]
};

export type TagBarItem = {
    tabName:string,
    index:number,
    paddingLeft?:String,
    paddingRight?:String,
    path: String | RouteLocationAsRelativeGeneric | RouteLocationAsPathGeneric | undefined
};

export type OptionInfo = {
    label: string,
    value: string | number
};

export type FilterItem = {
    selectList?: OptionInfo[][],
    switchList?: OptionInfo[]
};

export type ESResult = {
    document: any&{ id: string };
    highlight: any;
};

export type PeerOne = {
    name: string | null;
    avatar: string | null;
    rtcPeerConnection: RTCPeerConnection;
    hasRemoteSDP: boolean;
    hasLocalSDP: boolean;
    hasRemoteCandidate: boolean;
    hasLocalCandidate: boolean;
};

export type Room = {
    roomId: string;
    roomOwnerId: string;
    roomName: string;
    roomType: string;
    peerMap?: Map<string, PeerOne>;
};

export type RoomInvitation = {
    roomId: string;
    roomOwnerId: string;
    roomName: string;
    roomType: string;
    srcUserId: string;
    srcUserName: string | null;
    tgtUserId: string;
    tgtUserName: string;
};

export type ContextMenuOptions = {
    text: String | Ref<string>,
    callback: ()=>void
};