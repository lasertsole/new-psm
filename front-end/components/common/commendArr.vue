<template>
    <div class="commendArr">
        <div class="topBar">
            <div class="icon"></div>
            <span>评论</span> 
        </div>
        <template v-for="(item,index) in commendArr" :key="index">
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
    import type { Page } from "@/types/common";
    import type { Review } from "@/types/review";
    import { TargetTypeEnum } from "@/enums/review";

    const { ID, targetType } = defineProps({
        ID:{type:String, required:true},
        targetType:{type:Object as PropType<TargetTypeEnum>, required:true},
    });

    const pageSize = 50;
    const mainIsFinalPage: Ref<boolean> = ref<boolean>(false);// 主分页是否是最后一页
    const subIsFinalPage: Ref<boolean> = ref<boolean>(false);// 子分页是否是最后一页

    async function getReviewsEvent(current:number, size: number, attachUserId?:string):Promise<void> {
        // 如果是最后评论分页，则不请求
        if(attachUserId&&subIsFinalPage) {
            return;
        } else if(attachUserId == undefined && mainIsFinalPage ) {
            return;
        };

        const resultPage:Page<Review> = await getReviews({
            current, 
            size: pageSize,
            targetType,
            targetId:ID,
            attachUserId
        });

        // 判断是否是最后一页,如果是最后一页则设置标志位为true
        if(attachUserId && resultPage.total! < pageSize ) {
            subIsFinalPage.value = true;
        } else if(attachUserId == undefined && resultPage.total! < pageSize ) {
            mainIsFinalPage.value = true;
        };
    };

    onMounted(async ()=> {
        await getReviewsEvent(1, pageSize);
    });

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
    @use "@/common.scss" as common;

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
                @include common.fixedSquare(20px);
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