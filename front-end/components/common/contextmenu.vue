<template>
    <ul
        v-show="isVisible"
        :style="{ top: `${computedTop}px`, left: `${computedLeft}px` }"
        class="absolute z-2"
        ref="ulDom"
    >
        <template v-for="(item, index) in contextMenuOptions" :key="index">
            <li class="cursor-pointer" @click="item.callback()">{{ item.text }}</li>
        </template>
    </ul>
</template>

<script setup lang="ts">
    import type { PropType } from "vue";
    import type { ContextMenuOptions } from "@/types/common";

    const props = defineProps({
        UlClass: {type:String, required: false },
        liClass: {type:String, required: false },
        isVisible: { type:Boolean, required: true },
        top: { type:Number, required: true, default: 0 },
        left: { type:Number, required: true, default: 0 },
        contextMenuOptions: { type: Array as PropType<ContextMenuOptions[]>, required: true }
    });

    const ulDom:Ref<HTMLElement | undefined> = ref<HTMLElement | undefined>();

    const computedTop = ref<number>(0);// 计算上下位置
    const computedLeft = ref<number>(0);// 计算左右位置

    onMounted(()=>{
        if(props.UlClass) {
            props.UlClass.split(" ").filter(item=>item!="").forEach(item=>ulDom.value!.classList.add(item));
        };

        if(props.liClass) {
            const liArr:NodeListOf<HTMLLIElement> = ulDom.value!.querySelectorAll("li");
            props.liClass.split(" ").filter(item=>item!="").forEach(item=>liArr.forEach(li=>li.classList.add(item)));
        };

        watch(
            [() => props.top, ulDom],
            async ([newTop, newUlDom]) => {
                await nextTick();
                let windowHeight = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight); // 获取窗口高度
                let ulHeight = newUlDom ? newUlDom.getBoundingClientRect().height : 0; // 获取 ul 高度
                if (newTop + ulHeight > windowHeight) { // 修正底侧超出窗口情况
                    computedTop.value = newTop - ulHeight;
                } else {
                    computedTop.value = newTop;
                }
            },
            { immediate: true }// 初始化时执行一次
        );

        watch(
            [() => props.left, ulDom],
            async ([newLeft, newUlDom]) => {
                await nextTick();
                let windowWidth = (window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth);// 获取窗口宽度
                let ulWidth = newUlDom ? newUlDom.getBoundingClientRect().width : 0; // 获取 ul 高度
                if (newLeft + ulWidth > windowWidth) { // 修正底侧超出窗口情况
                    computedLeft.value = newLeft - ulWidth;
                } else {
                    computedLeft.value = newLeft;
                }
            },
            { immediate: true }// 初始化时执行一次
        );
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;

    ul{
        li{
            
        }
    }
</style>