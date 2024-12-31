import { TargetTypeEnum } from "@/enums/review";

export type Review = {
    id: string;
    srcUserId: string;
    avatar: string;
    name: string;
    targetType: TargetTypeEnum;
    targetId: string;
    attachUserId?: string | undefined;
    replyUserId?: string | undefined;
    content: string;
    timestamp: string;
    createTime: string;
};

export { TargetTypeEnum };