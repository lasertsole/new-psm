<template>
    <div class="workBox" @click="jumpToShowcaseDetail()">
        <CommonImage :src="cover"></CommonImage>
    </div>
</template>

<script setup lang="ts">
    import { defineProps, type PropType } from "vue";
    import { Picture as IconPicture } from '@element-plus/icons-vue';

    const props = defineProps({
        ID:{ type: String, required: true },
        title:{ type: String, required: true },
        cover:{ type: String, required: true },
        style:{ type: String, required: true },
        type:{ type: String, required: true },
    });

    function jumpToShowcaseDetail():void{
        navigateTo({ 
            name: 'model-3D-detailShow',
            params:{
                id:props.ID,
            }
        });
    }
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .workBox{
        box-sizing: border-box;
        @include fullWidth();
        @include fixedHeight(220px);
        display: flex;
        flex-direction: column;
        border: 2px solid #959595;
        border-radius: 10px;
        padding: 5px;
        cursor: pointer;
        
        &::v-deep(.el-image){
            @include fullInParent();
            
            .image-slot{
                @include fullInParent();
                display: flex;
                justify-content: center;
                align-items: center;

                .el-icon{
                    @include fullInParent();
                    @include flexCenter;

                    svg{
                        @include fixedSquare(20%);
                    }
                }
            }
        }
    }
</style>