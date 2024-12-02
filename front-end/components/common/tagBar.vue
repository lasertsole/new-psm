<template>
    <div class="classify">
        <div class="fullContain">
            <div :class="{tabBox:true, isInputFocus}">
                <div>
                    <template v-for="item,index in tabList">
                        <NuxtLink
                            :class="{selected:tabIndex==index}"
                            :style="`${item.paddingLeft?'padding-left:'+item.paddingLeft+';':'10px;'} ${item.paddingRight?'padding-right:'+item.paddingRight+';':'10px;'}`"
                            :to="item.path"
                            @click="changeTabIndex(index)"
                        >
                            {{ item.tabName }}
                        </NuxtLink>
                    </template>
                </div>
                <div class="lineBar">
                    <div class="line" :style="`width:${1/tabList.length*100}%; transform: translateX(${tabIndex*100}%);`"></div>
                </div>
            </div>

            <div :class="{searchBar:true, isInputFocus}">
                <el-input
                    placeholder="查找模型信息"
                    clearable
                    class="input-with-select"
                    @focus="inputFocus"
                    @blur="inputBlur"
                    v-model="searchText"
                    @input="searchBarInputEvent"
                    @keydown.enter="detaliSearchEvent"
                    @clear="clearEvent"
                >
                    <template #prepend>
                        <el-button
                            @focus="inputFocus"    
                            @click="detaliSearchEvent"
                            :icon="Search"
                        />
                    </template>
                </el-input>

                <el-skeleton v-show="isInputFocus" :rows="3" animated :loading="skeletonLoading">
                    <template #default>
                        <div v-show="isInputFocus&&!skeletonLoading&&showSearchBar" class="searchResult">
                            <template v-for="(item, index) in blurSearchResults" :key="index">
                                <CommonSearchItem
                                    :Id="item.id"
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
        <div :class="{fullLine:true, isInputFocus}"></div>
    </div>
</template>

<script setup lang="ts">
    import type { PropType } from "vue";
    import type { TagBarItem } from "@/types/common";
    import { Search } from '@element-plus/icons-vue';
    import { ref, defineProps, defineEmits, watchEffect } from "vue";

    const props = defineProps({
        tabList:{type:Array as PropType<TagBarItem[]>, required: true},
        focusIndex:{type:Number, required: false, default: 0},
        blurSearch: {type:Function, required: false},
        detaliSearch: {type:Function, required: false},
    });

    const emits = defineEmits(["changeClassifyIndex"]);

    const tabIndex = ref<number>(0);
    function changeTabIndex(index:number):void {
        tabIndex.value=index;
        emits("changeClassifyIndex", tabIndex.value);
    }

    watchEffect(()=>{
        tabIndex.value = props.focusIndex;
    });

    /******以下是搜索框部分******/
    const searchText:Ref<string> = ref<string>("");
    const isInputFocus:Ref<boolean> = ref<boolean>(false);
    const blurSearchResults: Ref<string[]> = ref<string[]>([]);
    function inputFocus():void {
        isInputFocus.value = true;
    };
    function inputBlur():void {
        isInputFocus.value = false;
    };

    const searchBarInputEvent = debounce(():void => {
        if(searchText.value=="") { 
            showSearchBar.value = false;
            return; 
        }; 

        
        blurSearch(searchText.value).then(()=>{
            showSearchBar.value = true;
        });
    }, 1000);

    const detaliSearchEvent = debounce(():void => {
        if(searchText.value=="") { ElMessage.warning('请输入搜索内容'); return }; 

        detaliSearch(searchText.value);
    }, 1000);


    const skeletonLoading:Ref<boolean> = ref<boolean>(false);
    async function blurSearch(keyword:string):Promise<void> {
        if(keyword=="") { return }; 
        skeletonLoading.value = true;
        props.blurSearch&&(blurSearchResults.value=await props.blurSearch(keyword));
        skeletonLoading.value = false;
    };

    async function detaliSearch(keyword:string):Promise<void> {
        if(keyword=="") { ElMessage.warning('请输入搜索内容'); return }; 
        props.detaliSearch&&props.detaliSearch(keyword);
    };

    const showSearchBar:Ref<boolean> = ref<boolean>(false);
    async function clearEvent():Promise<void> {
        blurSearchResults.value = [];
        showSearchBar.value = false;
    };
</script>

<style lang="scss" scoped>
    .classify{
        .fullContain{
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
            height: 45px;

            .tabBox{
                display: flex;
                flex-direction: column;
                height: 100%;
                position: relative;
                transition: all .3s;

                &.isInputFocus{
                    overflow: hidden;
                    transform: translateX(-100%);
                    width: 0%;
                    div{
                        overflow: hidden;
                        opacity: 0;
                    }
                }

                div{
                    height: 100%;
                    display: flex;
                    overflow: hidden;
                    a{
                        height: 100%;
                        overflow: hidden;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        padding: 10px;
                        cursor: pointer;
                        color: #707070;
                        word-wrap: break-word;
                        &.selected{
                            color: #00a8e9;
                        }
                    }
                }
                
                .lineBar{
                    height: 2px;
                    .line{
                        height: 100%;
                        background-color: #00a8e9;
                        transition: transform .3s ease;
                    }
                }
            }

            .searchBar{
                height: 100%;
                width: 230px;
                transition: all .3s ease-in;
                display: flex;
                flex-direction: column;
                position: relative;

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

                :deep(.el-skeleton) {
                    border: #00a8e9 1px solid;
                    border-radius: 0px 0px 5px 5px;
                    padding: 10px;
                }
            }
        }
        .fullLine{
            height: 1.5px;
            background-color: #a5a5a55b;
            position: relative;
            z-index: 1;
            transform: translateY(-1.75px);
            transition: opacity .3s;
            opacity: 1;

            &.isInputFocus{
                opacity: 0;
            }
        }
    }
</style>