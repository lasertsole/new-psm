<template>
    <div class="follow">
        <div class="items">
            <div class="filterBar"
                ref="filterBar"
            >
                <CommonFilterBar
                    :filterItem="filterItem"
                >
                </CommonFilterBar>
            </div>

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
    import type { ModelInfos } from "@/types/model";
    import type { Page, FilterItem } from "@/types/common";
    import { StyleEnum, TypeEnum } from "@/enums/models.d";

    // 样式标签列表
    const styleOpts = Object.entries(StyleEnum);
    // 类型标签列表
    const typeOpts = Object.entries(TypeEnum);

    const filterItem = ref<FilterItem>(
        {
            selectList:[
                [
                    ...styleOpts.map(item=>{
                        return {
                            label:item[0],
                            value: item[1]
                        }
                    })
                ],
                [
                    ...typeOpts.map(item=>{
                        return {
                            label:item[0],
                            value: item[1]
                        }
                    })
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
        name: 'model-3D-show-follow'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .follow{
        @include fullWidth();
        min-height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .items{
            @include fullWidth();
            min-height: 100%;
            justify-self: flex-start;
        }

        .pagination{
            display: flex;
            justify-content: center;
        }
    }
</style>