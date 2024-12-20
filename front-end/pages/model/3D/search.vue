<template>
    <div class="search">
        <div class="searchBar">
            <el-input
                placeholder="查找模型信息"
                clearable
                class="input-with-select"
                v-model="searchKeyword"
                @keydown.enter="detaliSearchEvent"
            >
                <template #prepend>
                    <el-button 
                        @click="detaliSearchEvent"
                        :icon="Search"
                    />
                </template>
            </el-input>
        </div>

        <el-main class="detailSearchResult" v-loading="loading">
            <div class=list
                v-infinite-scroll="load"
                :infinite-scroll-immediate="false"    
            >
                <template v-for="(item, index) in detailSearchResults.records">
                    <ModelSearchResultBox
                        :ID="item.document.id"
                    >
                        <template #title>
                            标题:<span v-html="item.highlight.title?item.highlight.title:item.document.title"></span>
                        </template>
                        <template #content>
                            简介:<span v-html="item.highlight.content?item.highlight.content:item.document.content"></span>
                        </template>
                        <template #cover>
                            <CommonImage :src="item.document.cover"></CommonImage>
                        </template>
                        <template #style>
                            风格:<span>{{StyleEnumObject[item.document.style]}}</span>
                        </template>
                        <template #type>
                            类型:<span>{{TypeEnumObject[item.document.type]}}</span>
                        </template>
                        <template #storage>
                            文件大小:<span>{{(item.document.storage/1024/1024).toFixed(2)}}</span>MB
                        </template>
                    </ModelSearchResultBox>
                </template>
            </div>
            <el-empty v-show="detailSearchResults.records?.length==0" description="未找到相关信息" />
        </el-main>
    </div>
</template>

<script setup lang="ts">
    import { Search } from '@element-plus/icons-vue';
    import type { ESResult, Page } from "@/types/common";
    import { StyleEnum, TypeEnum } from "@/enums/model3d.d";

    definePageMeta({
        name: 'model-3D-search',
    });

    /******以下是搜索框部分******/
    // 获取当前路由对象
    const route = useRoute();
    const pageCount: Ref<number> = ref<number>(1);
    // 从 query 参数中获取 text
    const searchKeyword: Ref<string> = ref<string>(Array.isArray(route.query.keyword) ? route.query.keyword[0] || "" : route.query.keyword || "");
    onActivated(() => {
        searchKeyword.value= Array.isArray(route.query.keyword) ? route.query.keyword[0] || "" : route.query.keyword || "";
        detaliSearch(searchKeyword.value, 1, 10);
    });

    const detailSearchResults: Ref<Page<ESResult>> = ref<Page<ESResult>>({
        total: 0,
        records: [] as ESResult[],
        size: 10,
        nextAfterKeys: []
    });
    const detaliSearchEvent = debounce(():void => {
        if(searchKeyword.value=="") { ElMessage.warning('请输入搜索内容'); return }; 
        pageCount.value=1;//重置页码
        isFinalPage.value=false;//重置是否是最后一页
        detaliSearch(searchKeyword.value, pageCount.value, 10);
    }, 1000);

    const isFinalPage: Ref<boolean> = ref<boolean>(false);
    async function detaliSearch(keyword:string, current: number, size: number):Promise<void> {
        if(keyword=="") { ElMessage.warning('请输入搜索内容'); return }; 
        if(isFinalPage.value) return;// 如果是末页，则不进行加载
        loading.value = true;

        let afterKey:string|null = detailSearchResults.value.nextAfterKeys!.length!=0?detailSearchResults.value.nextAfterKeys![0]:null;// 获取下一页的key
        let resultPage:Page<ESResult> = await detailSearchModel3d(keyword, afterKey, size);
        detailSearchResults.value.total = resultPage.total;
        resultPage!.records!.forEach(item=>{
            detailSearchResults.value.records!.push(item);
        });
        let nextAfterKeys: string[] = resultPage.nextAfterKeys!;
        detailSearchResults.value.nextAfterKeys = nextAfterKeys;
        if(nextAfterKeys.length==0) isFinalPage.value=true;// 如果没有下一页，则设置为true

        loading.value = false;
    };

    async function load():Promise<void> {
        console.log("加载更多");
        pageCount.value+=1;
        detaliSearch(searchKeyword.value, pageCount.value, 10);
    }
    /******以上是搜索框部分******/

    // 转换为对象
    const StyleEnumObject = Object.fromEntries(
        Object.entries(StyleEnum).map(([key, value]) => [value, key])
    );
    // 转换为对象
    const TypeEnumObject = Object.fromEntries(
        Object.entries(TypeEnum).map(([key, value]) => [value, key])
    );

    // 加载状态
    const loading:Ref<boolean> = ref<boolean>(false);
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .search{
        @include fullInParent;
        background-color: white;
        display: flex;
        align-items: center;
        flex-direction: column;
        padding: 30px 20px 10px;

        >*{
            @include fullWidth;
        }

        .searchBar{
            transition: all .3s ease-in;
            display: flex;
            flex-direction: column;
            position: relative;
            height: 52px;
            padding-bottom: 10px;

            &.isInputFocus{
                width: 100%;
            }

            >div{
                background-color: white;
            }

            .el-input{
                width: 100%;
            }

            .searchResult{
                border: #00a8e9 1px solid;
                border-radius: 0px 0px 5px 5px;
                padding: 10px;
            }

            .skeleton{
                :deep(.el-skeleton) {
                    border: #00a8e9 1px solid;
                    border-radius: 0px 0px 5px 5px;
                    padding: 10px;
                    overflow: hidden;
                }
            }
        }

        .detailSearchResult{
            @include scrollBar(8px);
            padding: 0px 40px 20px 40px;
            flex-grow: 1;

            .list{
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(340px, 1fr)); // 自动适应列数，每列最小宽度为 100px
                font-size: 14px;
                gap: 15px;
                color: #707070;
            }

            :deep(.el-empty){
                height: 100%;
            }
        }

        .pagination{
            display: flex;
            justify-content: center;
        }
    }
</style>