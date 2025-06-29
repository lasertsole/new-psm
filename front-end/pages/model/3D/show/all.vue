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

            <div class="list">
                <template v-for="(item, index) in ModelShowItems.records" :key="index">
                    <ModelShowItem
                        :boxInfo="item"
                        :style="style"
                        :type="type"
                        :isIdle="isIdle"
                        :canUrgent="canUrgent"
                    >
                    </ModelShowItem>
                </template>
                <el-empty v-show="ModelShowItems.records?.length==0" description="未找到相关信息" />
            </div>
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
    import type { Model3DInfos } from "@/types/model3d";
    import type { Page, FilterItem } from "@/types/common";
    import { StyleEnum, TypeEnum } from "@/enums/model3d.d";

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
    
    const ModelShowItems: Ref<Page<Model3DInfos>> = ref<Page<Model3DInfos>>({records:[]});

    // SSR
    fetchModelsShowBars({current:1, size:10, isIdle:true, canUrgent:true});

    // 分页请求数据函数
    function fetchModelsShowBars({current, size, style, type, isIdle, canUrgent}:
        {current: number, size: number, style?:string, type?:string, isIdle:boolean, canUrgent:boolean}):void
    {
        loading.value = true;
        getModel3dsShowBars({current, size, style, type, isIdle, canUrgent}).then((res:Page<Model3DInfos>)=>{
            ModelShowItems.value = res;
        }).finally(()=>{loading.value = false;});
    };

    const currentPage: Ref<number> = ref<number>(1);
    const pageSize: Ref<number> = ref<number>(10);
    const style: Ref<string | undefined> = ref<string>();
    const type: Ref<string | undefined> = ref<string>();
    const isIdle: Ref<boolean> = ref<boolean>(true);
    const canUrgent: Ref<boolean> = ref<boolean>(true);

    async function handleSizeChange(): Promise<void> {
        loading.value = true;
        fetchModelsShowBars({
            current: currentPage.value, 
            size: pageSize.value,
            style: style.value,
            type: type.value,
            isIdle: isIdle.value,
            canUrgent: canUrgent.value
        });
    }

    async function handleCurrentChange(): Promise<void> {
        fetchModelsShowBars({
            current: currentPage.value, 
            size: pageSize.value,
            style: style.value,
            type: type.value,
            isIdle: isIdle.value,
            canUrgent: canUrgent.value
        });
    }

    async function handerFilterCommit({ typeArr, switchArr }: { typeArr:string[], switchArr: boolean[] }): Promise<void> {
        style.value = typeArr[0];
        type.value = typeArr[1];
        isIdle.value = switchArr[0];
        canUrgent.value = switchArr[1];

        fetchModelsShowBars({
            current: currentPage.value, 
            size: pageSize.value,
            style: style.value,
            type: type.value,
            isIdle: isIdle.value,
            canUrgent: canUrgent.value
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
    @use "@/common.scss" as common;

    .all{
        @include common.scrollBar(8px);
        @include common.fullWidth();
        min-height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .items{
            @include common.fullWidth();
            min-height: 100%;
            justify-self: flex-start;
            padding-bottom: 10px;
            display: flex;
            flex-direction: column;
            
            .filterBar{
                padding: 0px 20px 0px 20px;
            }

            .list{
                height: 200px;
                flex-grow: 1;
                padding: 0px 20px 30px 20px;
                @include common.scrollBar(8px);
                display: flex;
                flex-direction: column;
                overflow: auto;

                :deep(.el-empty){
                    height: 100%;
                }
            }
        }

        .pagination{
            display: flex;
            justify-content: center;
        }
    }
</style>