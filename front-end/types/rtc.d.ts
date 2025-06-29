export type Devices = {
    video: VideoOption[],
    audio: AudioOption[]
};

export type VideoOption = {
    name: string, active: string | undefined, type: 'webcam' | 'screen', bindStreams: MediaStreamTrack[]
};

export type AudioOption = {
    name: string, active: string | undefined, type: 'microphone' | 'speaker', bindStreams: MediaStreamTrack[]
};

export type RTCSwap = {
    roomId: string;
    srcUserId: string;
    srcUserName: string | null;
    srcUserAvatar: string| null;
    tgtUserId: string;
    data: string;
};