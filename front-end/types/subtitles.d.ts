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

export enum PrimarySort{
    "全部字幕" = 0,
    "我关注的" = 1
}

export enum SortWay{
    "按时间" = 0,
    "按评论数" = 1,
}