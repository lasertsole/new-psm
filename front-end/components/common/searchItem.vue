<template>
    <div class="searchItem" @click="jumpToShowcaseDetail">
        <div class="searchItem-title">
            <slot name="title"></slot>
        </div>
        <div class="searchItem-content">
            <slot name="content"></slot>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        ID: {type:String, required: true},
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

    .searchItem{
        font-size: 14px;
        padding: 5px 0px;
        &:hover{
            background-color: #f5f5f5;
        }

        >div{
            display: flex;
            align-items: center;
            justify-content: flex-start;
        }
    }
</style>