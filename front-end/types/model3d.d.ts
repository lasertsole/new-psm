import type { UserInfo } from "@/types/user";

export type Model3DInfo = {
    id?: string;
    userId?: string;
    title?: string;
    content?: string;
    cover?: Blob | string;
    entity?: Blob | string;
    style?: string;
    type?: string;
    visible?: string;
    createTime?: string;
};

export type Model3DInfos = {
    user: UserInfo;
    models: ModelInfo[];
}

export type Model3DInfoDetail = {
    user: UserInfo;
    model: ModelInfo;
}