export type Devices = {
    video: VideoOption[],
    audio: AudioOption[]
};

export type VideoOption = {
    name: string, active: string | undefined, type: 'webcam' | 'screen'
};

export type AudioOption = {
    name: string, active: string | undefined
};