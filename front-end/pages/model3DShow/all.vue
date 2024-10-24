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
    import type { ModelInfo } from "@/types/model";
    
    const ModelShowItems:Ref<ModelInfo[]> = ref<ModelInfo[]>([] as ModelInfo[]);
    
    onMounted(async ()=>{
        ModelShowItems.value = await getModelsShowBar({currentPage:1, pageSize:10}); 
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    
</style>