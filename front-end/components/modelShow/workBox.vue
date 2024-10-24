<template>
    <div class="workBox" @click="jumpToShowcaseDetail()">
        <el-image :src="cover" fit="cover">
            <template #error>
                <div class="image-slot">
                    <el-icon><icon-picture/></el-icon>
                </div>
            </template>
        </el-image>
    </div>
</template>

<script setup lang="ts">
    import { defineProps } from "vue";
    import { Picture as IconPicture } from '@element-plus/icons-vue';
    
    const props = defineProps({
        ID:{ type: Number, required: true },
        title:{ type: String, required: true },
        content:{ type: String, required: true },
        cover:{ type: String, required: true },
        entity:{ type: String, required: true },
        category:{ type: String, required: true }
    });

    function jumpToShowcaseDetail():void{
        navigateTo({ 
            name: 'model3DDetailShow',
            query:{
                ID:props.ID,
                title:props.title,
                content:props.content,
                cover:props.cover,
                entity:props.entity,
                category:props.category
            }
        });
    }
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .workBox{
        box-sizing: border-box;
        @include fixedWidth(200px);
        height: 150px;
        display: flex;
        flex-direction: column;
        border: 2px solid #959595;
        border-radius: 10px;
        padding: 5px;
        margin-left: 15px;
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

                    svg{
                        @include fixedSquare(20%);
                    }
                }
            }
        }
    }
</style>