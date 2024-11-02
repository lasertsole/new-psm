<template>
    <template v-for="(item, index) in ModelShowItems" :key="index">
        <ModelShowItem
            :boxInfo="item"
        >
        </ModelShowItem>
    </template>
</template>

<script setup lang="ts">
    import { onMounted } from "vue";
    import type { ModelInfos } from "@/types/model";
    
    const ModelShowItems:Ref<ModelInfos[]> = ref<ModelInfos[]>([] as ModelInfos[]);

    // 服务器渲染请求
    ModelShowItems.value = await getModelsShowBars({currentPage:1, pageSize:10});
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