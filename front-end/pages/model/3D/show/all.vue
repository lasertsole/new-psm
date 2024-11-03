<template>
    <template v-for="(item, index) in ModelShowItems.records" :key="index">
        <ModelShowItem
            :boxInfo="item"
        >
        </ModelShowItem>
    </template>
</template>

<script setup lang="ts">
    import { onMounted } from "vue";
    import type { ModelInfos } from "@/types/model";
    import type { Page } from "@/types/common";
    
    const ModelShowItems:Ref<Page<ModelInfos>> = ref<Page<ModelInfos>>({records:[]});
    
    // 服务器渲染请求
    ModelShowItems.value = (await getModelsShowBars({current:1, size:10}));
    
    onMounted(async ()=>{
    });

    definePageMeta({
        name: 'model-3D-show-all'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    
</style>