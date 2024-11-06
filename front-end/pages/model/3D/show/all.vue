<template>
    <div class="all">
        <div class="items">
            <template v-for="(item, index) in ModelShowItems.records" :key="index">
                <ModelShowItem
                    :boxInfo="item"
                >
                </ModelShowItem>
            </template>
        </div>

        <el-pagination 
            class="pagination"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :page-size="pageSize"
            :total="ModelShowItems.total"
            v-model:current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
    </div>
</template>

<script setup lang="ts">
    import { onMounted } from "vue";
    import type { ModelInfos } from "@/types/model";
    import type { Page } from "@/types/common";
    
    const ModelShowItems:Ref<Page<ModelInfos>> = ref<Page<ModelInfos>>({records:[]});
    
    // 服务器渲染请求
    ModelShowItems.value = (await getModelsShowBars({current:1, size:10}));

    const currentPage:Ref<number> = ref<number>(1);
    const pageSize:Ref<number> = ref<number>(10);

    const handleSizeChange = async() => {
        ModelShowItems.value = (await getModelsShowBars({current:currentPage.value, size:pageSize.value}));
    }
    const handleCurrentChange = async() => {
        ModelShowItems.value = (await getModelsShowBars({current:currentPage.value, size:pageSize.value}));
    }

    onMounted(async ()=>{
    });

    definePageMeta({
        name: 'model-3D-show-all'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .all{
        @include fullWidth();
        min-height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .items{
            @include fullWidth();
            min-height: 100%;
        }

        .pagination{
            display: flex;
            justify-content: center;
        }
    }
</style>