<template>
    <div class="showcase">
        <CommonTagBar
            :tabList="tabList"
            @changeClassifyIndex="changeClassifyIndex"
        >
        </CommonTagBar>
    </div>
</template>

<script setup lang="ts">
    import type { TagBarItem } from "@/types/common";
    import { PrimarySort } from "@/types/subtitles.d";

    const tabList = ref<TagBarItem[]>([
        {
            tabName:PrimarySort[0],
            index:0,
            paddingRight:"37.5px"
        },
        {
            tabName:PrimarySort[1],
            index:1,
            paddingLeft:"37.5px"
        },
    ]);

    const classifyIndex = ref<number>(0);
    function changeClassifyIndex(index: number):void{
        classifyIndex.value = index;
    }

</script>

<style lang="scss" scoped>
    .showcase{
        width: 100%;
        height: 100%;
        box-sizing: border-box;
        overflow-y: auto;
        padding: 30px 80px;
        display: flex;
        flex-direction: column;
    }
</style>