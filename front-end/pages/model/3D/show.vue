<template>
    <div class="showcase">
        <CommonTagBar
            :tabList="tabList"
        >
        </CommonTagBar>

        <CommonFilterBar
            :filterItem="filterItem"
        >
        </CommonFilterBar>

        <NuxtPage keepalive/>
    </div>
</template>

<script setup lang="ts">
    import type { TagBarItem, FilterItem } from "@/types/common";
    import { PrimarySort } from "@/enums/subtitles.d";
    import { SortWay } from "@/enums/subtitles.d";
    
    const tabList = ref<TagBarItem[]>([
        {
            tabName:PrimarySort[0],
            index:0,
            paddingRight:"37.5px",
            path:"/model/3D/show/all"
        },
        {
            tabName:PrimarySort[1],
            index:1,
            paddingLeft:"37.5px",
            path:"/model/3D/show/follow"
        },
    ]);

    const filterItem = ref<FilterItem>(
        {
            selectList:[
                [
                    {
                        label: '直播切片',
                        value: 0,
                    },
                    {
                        label: '歌曲相关',
                        value: 1,
                    },
                    {
                        label: '视频后期',
                        value: 2,
                    }
                ],
                [
                    {
                        label: SortWay[0],
                        value: 0,
                    },
                    {
                        label: SortWay[1],
                        value: 1,
                    }
                ]
            ],
            switchList:[
                {
                    label: "档期空闲",
                    value: 0,
                },
                {
                    label: "能否加急",
                    value: 1,
                }
            ]
        }
    );

    definePageMeta({
        name: 'model-3D-show'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
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