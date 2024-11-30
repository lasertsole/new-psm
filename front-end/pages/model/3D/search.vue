<template>
    <div class="search">
        <div class="searchBar">
            <el-input
                placeholder="查找模型信息"
                clearable
                v-model="searchKeyword"
                @keydown.enter="detailSearch()"
            >
                <template #prepend>
                    <el-button :icon="Search" @click="detailSearch"/>
                </template>
            </el-input>
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
        if(searchKeyword.value=="") { ElMessage.warning('请输入搜索内容'); return; }; 
        
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