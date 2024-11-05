import type { UserInfo } from "@/types/user";

export type ModelInfo = {
    id?: string;
    userId?: string;
    title?: string;
    content?: string;
    cover?: Blob | string;
    entity?: Blob | string;
    style: string;
    type: string;
    visible?: string;
    createTime?: string;
};

export type ModelInfos = {
    user: UserInfo;
    models: ModelInfo[];
}

export type ModelInfoDetail = {
    user: UserInfo;
    model: ModelInfo;
}