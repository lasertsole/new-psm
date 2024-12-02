<template>
    <div class="search">
        <div class="searchBar">
            <el-input
                placeholder="查找模型信息"
                clearable
                v-model="searchKeyword"
                @input="blurSearch"
                @keydown.enter="detailSearch"
            >
                <template #prepend>
                    <el-button :icon="Search" @click="detailSearch"/>
                </template>
            </el-input>

            <el-skeleton v-show="isInputFocus" :rows="3" animated :loading="skeletonLoading">
                <template #default>
                    <div v-show="isInputFocus&&!skeletonLoading&&showSearchBar" class="searchResult">
                        <template v-for="(item, index) in blurSearchResults" :key="index">
                            <CommonSearchItem
                                :Id="item.document.id"
                            >
                                <template v-slot:title v-show="item.highlight.title">
                                    <template v-for="(subItem,subIndex) in item.highlight.title" :key="subIndex">
                                        标题: <div v-html="subItem"></div>
                                    </template>
                                </template>
                                <template v-slot:content v-show="item.highlight.content">
                                    <template v-for="(subItem,subIndex) in item.highlight.content" :key="subIndex">
                                        介绍: <span v-html="subItem"></span>
                                    </template>
                                </template>
                            </CommonSearchItem>
                        </template>
                        <div v-show="blurSearchResults.length==0">未查询到对应结果</div>
                    </div>
                </template>
            </el-skeleton>
        </div>
    </div>
</template>

<script setup lang="ts">
    import { Search } from '@element-plus/icons-vue';
    import type { Model3DInfo } from "@/types/model3d";

    definePageMeta({
        name: 'model-3D-search',
    });

    // 获取当前路由对象
    const route = useRoute();
    // 从 query 参数中获取 text
    const searchKeyword: Ref<string> = ref<string>(Array.isArray(route.query.keyword) ? route.query.keyword[0] || "" : route.query.keyword || "");
    onActivated(() => {
        searchKeyword.value= Array.isArray(route.query.keyword) ? route.query.keyword[0] || "" : route.query.keyword || "";
    });

    const blurSearchBars:Ref<Model3DInfo[]> = ref<Model3DInfo[]>([]);
    const detaliSearchBars:Ref<Model3DInfo[]> = ref<Model3DInfo[]>([]);

    const blurSearch = debounce(async():Promise<void>=> {
        if(searchKeyword.value=="") return;
        
        blurSearchBars.value = await blurSearchModel3d(searchKeyword.value);
    }, 1000);

    const detailSearch = debounce(async():Promise<void>=> {
        if(searchKeyword.value=="") { ElMessage.warning('请输入搜索内容'); return; };

        detaliSearchBars.value = await detailSearchModel3d(searchKeyword.value);
    }, 1000);
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .search{
        @include fullInParent;
        padding: 30px 20px;
        background-color: white;

        .searchBar{
            em{
                background-color: yellow;
            }
        }
    }
</style>