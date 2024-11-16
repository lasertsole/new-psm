<template>
    <div class="show"
        ref="show"
    >
        <div class="tagBar"
            ref="tagBar"
        >
            <CommonTagBar
                :tabList="tabList"
            >
            </CommonTagBar>
        </div>

        <div class="content" ref="content">
            <NuxtPage keepalive/>
        </div>
    </div>
</template>

<script setup lang="ts">
    import type { TagBarItem } from "@/types/common";
    import { StyleEnum, TypeEnum, PrimarySort } from "@/enums/model3d.d"
    
    // 样式标签列表
    const styleOpts = Object.entries(StyleEnum);
    // 类型标签列表
    const typeOpts = Object.entries(TypeEnum);

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


    
    const show: Ref<HTMLElement | undefined> = ref<HTMLElement>();
    const tagBar: Ref<HTMLElement | undefined> = ref<HTMLElement>();
    const content: Ref<HTMLElement | undefined> = ref<HTMLElement>();
    const contentHeight: Ref<string> = ref<string>('');

    async function computeHeight():Promise<void> {
        await nextTick();
        if(!show.value || !tagBar.value) return
        contentHeight.value = show.value.parentElement!.clientHeight
        - Number(window.getComputedStyle(show.value).paddingTop.replace('px',''))
        - Number(window.getComputedStyle(show.value).paddingBottom.replace('px',''))
        - tagBar.value.clientHeight
        + 'px';
    }

    onActivated (async ()=>{
        await computeHeight();

        window.addEventListener('resize', computeHeight);
    });

    onDeactivated (()=>{
        window.removeEventListener('resize', computeHeight);
    });

    definePageMeta({
        name: 'model-3D-show'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .show{
        border-radius: 5px;
        padding: 20px;
        @include fullInParent;
        box-sizing: border-box;
        display: flex;
        flex-direction: column;
        background-color: white;

        @media screen and (max-width: 800px){
            padding: 30px 0px;
        }

        .content{
            $contentHeight: v-bind(contentHeight);
            height: $contentHeight;
        }

        .pagination{
            align-self: center;
        }
    }
</style>