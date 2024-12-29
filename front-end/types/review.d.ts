import { TargetTypeEnum } from "@/enums/review";

export type Review = {
    id: String;
    srcUserId: String;
    avatar: String;
    name: String;
    targetType: TargetTypeEnum;
    targetId: String;
    timestamp: String;
    content: String;
    createTime: String;
};

export { TargetTypeEnum };