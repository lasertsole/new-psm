<template>
    <div class="workBox" @click="jumpToShowcaseDetail">
        <div class="cover">
            <slot name="cover"></slot>
        </div>
        <div class="classify">
            <div :class="{ yellow: optionStyle==style }">
                <span>风格: </span>
                <span>{{ StyleEnumObject[style] }}</span>
            </div>
            <div :class="{ yellow: optionType==type }">
                <span>类型: </span>
                <span>{{ TypeEnumObject[type] }}</span>
            </div>
            <div>
                <span>创建时间: </span>
                <span>{{ createTime }}</span>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
    import { defineProps } from "vue";
    import { StyleEnum, TypeEnum } from "@/enums/model3d.d";

    // 转换为对象
    const StyleEnumObject = Object.fromEntries(
        Object.entries(StyleEnum).map(([key, value]) => [value, key])
    );
    // 转换为对象
    const TypeEnumObject = Object.fromEntries(
        Object.entries(TypeEnum).map(([key, value]) => [value, key])
    );

    const props = defineProps({
        ID: {
            type: String,
            required: true,
            validator: function (value:string):boolean {
                try {
                    return parseInt(value)>=0;
                } catch (error) {
                    return false;
                };
            }
        },
        title: { type: String, required: true },
        createTime: { type: String, required: true },
        style: { type: String, required: true },
        type: { type: String, required: true },
        optionStyle: { type: String, required: true },
        optionType: { type: String, required: true },
    });

    function jumpToShowcaseDetail():void{
        navigateTo({ 
            name: 'model-3D-detailShow',
            params:{
                id:props.ID,
            }
        });
    };
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;
    
    .workBox{
        @include common.fullWidth();
        @include common.fixedHeight(220px);
        display: flex;
        flex-direction: column;
        border: 2px solid #959595;
        border-radius: 10px;
        padding: 5px;
        cursor: pointer;

        .cover{
            @include common.fullWidth();
            @include common.fixedHeight(190px);
            &::v-deep(.el-image){
                @include common.fullInParent();
                
                .image-slot{
                    @include common.fullInParent();
                    display: flex;
                    justify-content: center;
                    align-items: center;

                    .el-icon{
                        @include common.fullInParent();
                        @include common.flexCenter;

                        svg{
                            @include common.fixedSquare(20%);
                        }
                    }
                }
            }
        }

        .classify{
            padding: 0px 5px;
            font-size: 12px;
            display: flex;
            justify-content: space-between;

            .yellow{
                background-color: yellow;
            }
        }
    }
</style>