import type { Page } from "@/types/common";
import type { UserInfo } from "@/types/user";

export type ModelInfo = {
    id?: string;
    userId?: string;
    title?: string;
    content?: string;
    cover?: Blob | string;
    entity?: Blob | string;
    category?: Category;
    visible?: string;
    createTime?: string;
};

export type Category = {
    style: string;
    type: string;
}

export type ModelInfos = {
    user: UserInfo;
    models: ModelInfo[];
}

export type ModelInfoDetail = {
    user: UserInfo;
    model: ModelInfo;
}