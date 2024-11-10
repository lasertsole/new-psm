<template>
    <div class="all">
        <el-main class="items" v-loading="loading">
            <div class="filterBar"
                ref="filterBar"
            >
                <CommonFilterBar
                    :filterItem="filterItem"
                    @filterCommit="handerFilterCommit"
                >
                </CommonFilterBar>
            </div>

            <template v-for="(item, index) in ModelShowItems.records" :key="index">
                <ModelShowItem
                    :boxInfo="item"
                >
                </ModelShowItem>
            </template>
        </el-main>

        <el-pagination 
            class="pagination"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="ModelShowItems.total"
            v-model:page-size="pageSize"
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

    // 加载状态
    const loading:Ref<boolean> = ref<boolean>(true);

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
    
    const ModelShowItems: Ref<Page<ModelInfos>> = ref<Page<ModelInfos>>({records:[]});
    
    // 分页请求数据函数
    function fetchModelsShowBars({current, size, style, type, isIdle, canUrgent}:
    {current: number, size: number, style?:string, type?:string, isIdle?:boolean, canUrgent?:boolean}):void
    {
        loading.value = true;
        getModelsShowBars({current, size, style, type, isIdle, canUrgent}).then((res)=>{
            ModelShowItems.value = res;
        }).finally(()=>{loading.value = false;});
    };

    // 服务器渲染请求
    fetchModelsShowBars({current:1, size:10});

    const currentPage: Ref<number> = ref<number>(1);
    const pageSize: Ref<number> = ref<number>(10);
    const style: Ref<string | undefined> = ref<string>();
    const type: Ref<string | undefined> = ref<string>();
    const isIdle: Ref<boolean> = ref<boolean>(false);
    const canUrgent: Ref<boolean> = ref<boolean>(false);

    async function handleSizeChange(): Promise<void> {
        loading.value = true;
        fetchModelsShowBars({
            current:currentPage.value, 
            size:pageSize.value,
            style:style.value,
            type:type.value,
            isIdle:isIdle.value,
            canUrgent:canUrgent.value
        });
    }

    async function handleCurrentChange(): Promise<void> {
        fetchModelsShowBars({
            current:currentPage.value, 
            size:pageSize.value,
            style:style.value,
            type:type.value,
            isIdle:isIdle.value,
            canUrgent:canUrgent.value
        });
    }

    async function handerFilterCommit({ typeArr, switchArr }: { typeArr:string[], switchArr: boolean[] }): Promise<void> {
        console.log(typeArr, switchArr);
        style.value = typeArr[0];
        type.value = typeArr[1];
        isIdle.value = switchArr[0];
        canUrgent.value = switchArr[1];

        fetchModelsShowBars({
            current:currentPage.value, 
            size:pageSize.value,
            style:style.value,
            type:type.value,
            isIdle:isIdle.value,
            canUrgent:canUrgent.value
        });
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
            justify-self: flex-start;
            
            .filterBar{
                margin-top: 10px;
            }
        }

        .pagination{
            display: flex;
            justify-content: center;
        }
    }
</style>