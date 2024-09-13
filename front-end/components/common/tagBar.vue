<template>
    <div class="classify">
        <div class="full-contain">
            <div class="tabBox">
                <div>
                    <template v-for="item,index in tabList">
                        <NuxtLink
                            :class="{selected:tabIndex==index}"
                            :style="`${item.paddingLeft?'padding-left:'+item.paddingLeft+';':'10px;'} ${item.paddingRight?'padding-right:'+item.paddingRight+';':'10px;'}`"
                            :to="item.path"
                            @click="changeTabIndex(index)"
                        >
                            {{ item.tabName }}
                        </NuxtLink>
                    </template>
                </div>
                <div class="lineBar">
                    <div class="line" :style="`width:${1/tabList.length*100}%; transform: translateX(${tabIndex*100}%);`"></div>
                </div>
            </div>
        </div>
        <div class="full-line"></div>
    </div>
</template>

<script setup lang="ts">
    import { ref, defineProps, defineEmits, watchEffect } from "vue";
    import type { PropType } from "vue";
    import type { TagBarItem } from "@/types/common";

    const props = defineProps({
        tabList:{type:Array as PropType<TagBarItem[]>, required: true},
        focusIndex:{type:Number, required: false, default: 0},
    });

    const emits = defineEmits(["changeClassifyIndex"]);

    const tabIndex = ref<number>(0);
    function changeTabIndex(index:number):void{
        tabIndex.value=index;
        emits("changeClassifyIndex", tabIndex.value);
    }

    watchEffect(()=>{
        tabIndex.value = props.focusIndex;
    })
</script>

<style lang="scss" scoped>
    .classify{
        .full-contain{
            display: flex;
            flex-direction: row;
            .tabBox{
                display: flex;
                flex-direction: column;
                div{
                    display: flex;
                    a{
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        padding: 10px;
                        cursor: pointer;
                        color: #707070;
                        &.selected{
                            color: #00a8e9;
                        }
                    }
                }
                .lineBar{
                    height: 2px;
                    .line{
                        height: 100%;
                        background-color: #00a8e9;
                        transition: transform .3s ease;
                    }
                }
            }
        }
        .full-line{
            height: 1.5px;
            background-color: #a5a5a55b;
            position: relative;
            z-index: 1;
            transform: translateY(-1.75px);
        }
    }
</style>