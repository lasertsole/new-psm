import { TargetTypeEnum, AttitudeTypeEnum } from "@/enums/review";

export type Review = {
    id: string;
    srcUserId: string;
    avatar: string;
    name: string;
    targetType: TargetTypeEnum;
    targetId: string;
    attachId?: string | undefined;
    replyId?: string | undefined;
    content: string;
    createTime: string;
    attaches?: Review[];
    replies?: Review[];
    likeNum: number;
    dislikeNum?: number;
};

export type Attitude = {
    id: string;
    srcUserId: string;
    targetType: TargetTypeEnum;
    targetId: string;
    attitudeType: AttitudeTypeEnum;
    createTime: string;
};

export { TargetTypeEnum, Attitude };