export type SubtitlesBox = {
    id?: string;
    userId?: string;
    title?: string;
    content?: string;
    coverUrl?: string;
    videoUrl?: string;
    cover?: Blob;
    video?: Blob;
    category?: string;
};

export type optionInfo = {
    label:string,
    value:number,
}