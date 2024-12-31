<template>
    <NuxtLink :to="{name: 'model-3D-detailShow', params: {id: ID}}" class="searchResultBox">
        <div class="leftBox">
            <slot name="cover"></slot>
        </div>
        <div class="rightBox">
            <div class="title"><slot name="title"></slot></div>
            <div class="content"><slot name="content"></slot></div>
            <div class="style"><slot name="style"></slot></div>
            <div class="type"><slot name="type"></slot></div>
            <div class="storage"><slot name="storage"></slot></div>
        </div>
    </NuxtLink>
</template>

<script setup lang="ts">
    const props = defineProps({
        ID: {type:String, required: true},
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;

    .searchResultBox{
        @include common.fixedHeight(150px);
        display: flex;
        overflow: hidden;
        border: 1px solid #00a8e9;
        border-radius: 8px;

        .leftBox{
            @include common.fixedRetangle(200px, 150px);
        }

        .rightBox{
            display: flex;
            flex-direction: column;
            padding: 10px;
        }
    }
</style>