import { TargetTypeEnum } from "@/enums/model3d";

export type Review = {
    id: String;
    srcUserId: String;
    targetType: TargetTypeEnum;
    targetId: String;
    timestamp: String;
    content: String;
    createTime: String;
};