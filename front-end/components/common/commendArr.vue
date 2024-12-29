<template>
    <div class="commendArr">
        <div class="topBar">
            <div class="icon"></div>
            <span>橱窗评价</span> 
        </div>
        <template v-for="(item,index) in commendArr">
            <comment
                :ID="item.id"
                :srcUserId="item.srcUserId"
                :avatar="item.avatar"
                :name="item.name"
                :targetType="item.targetType"
                :targetId="item.targetId"
                :timestamp="item.timestamp"
                :content="item.content"
                :createTime="item.createTime"
            ></comment>
        </template>
    </div>
</template>

<script setup lang="ts">
    import { ref } from "vue";
    import type { Review } from "@/types/review";
    import { TargetTypeEnum } from "@/enums/review";

    const commendArr = ref<Review[]>([
        {
            id: "112233",
            srcUserId: "112233",
            avatar: "",
            name: "moya",
            targetType: TargetTypeEnum.Model3d,
            targetId: "112233",
            timestamp: "2023-05-23",
            content: "又快又美，吹爆！！",
            createTime: "2023-05-23",
        },
        {
            id: "112233",
            srcUserId: "112233",
            avatar: "",
            name: "moya",
            targetType: TargetTypeEnum.Model3d,
            targetId: "112233",
            timestamp: "2023-05-23",
            content: "又快又美，吹爆！！",
            createTime: "2023-05-23",
        },
        {
            id: "112233",
            srcUserId: "112233",
            avatar: "",
            name: "moya",
            targetType: TargetTypeEnum.Model3d,
            targetId: "112233",
            timestamp: "2023-05-23",
            content: "又快又美，吹爆！！",
            createTime: "2023-05-23",
        },
        {
            id: "112233",
            srcUserId: "112233",
            avatar: "",
            name: "moya",
            targetType: TargetTypeEnum.Model3d,
            targetId: "112233",
            timestamp: "2023-05-23",
            content: "又快又美，吹爆！！",
            createTime: "2023-05-23",
        },
        {
            id: "112233",
            srcUserId: "112233",
            avatar: "",
            name: "moya",
            targetType: TargetTypeEnum.Model3d,
            targetId: "112233",
            timestamp: "2023-05-23",
            content: "又快又美，吹爆！！",
            createTime: "2023-05-23",
        },
        {
            id: "112233",
            srcUserId: "112233",
            avatar: "",
            name: "moya",
            targetType: TargetTypeEnum.Model3d,
            targetId: "112233",
            timestamp: "2023-05-23",
            content: "又快又美，吹爆！！",
            createTime: "2023-05-23",
        },
    ]);
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .commendArr{
        .topBar{
            height: 20px;
            box-sizing: content-box;
            padding-bottom: 20px;
            margin-bottom: 20px;
            font-size: 16px;
            font-weight: bold;
            border-bottom: 1px solid #ececec;
            display: flex;
            justify-content: flex-start;
            align-items: center;
            color: #4a4a4a;

            .icon{
                @include fixedSquare(20px);
                background-image: url(icons/showcaseComment.svg);
                background-size: 100%;
                background-position: center;
                margin-right: 10px;
            }
        }

        flex-grow: 1;
        padding:10px;
        display: flex;
        flex-direction: column;
        
        &::v-deep(.commend:not(:last-of-type)){
            margin-bottom: 13px;
            border-bottom: 1px solid lightgray;
        }
    }
</style>