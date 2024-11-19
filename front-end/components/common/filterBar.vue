<template>
    <div class="filter-bar">
        <template v-for="(item,index) in filterItem.selectList" :key="index">
            <el-select
                class="m-2" 
                clearable
                v-model="typeValue[index]" 
                placeholder="no select"
                size="small">
                
                <el-option
                    v-for="subItem in item"
                    :key="subItem.label"
                    :label="subItem.label"
                    :value="subItem.value"
                />
            </el-select>
        </template>

        <el-switch
            v-for="(item,index) in filterItem.switchList"
            v-model="switchValue[index]"
            :key="item.label"
            :active-text="item.label"
        />

        <el-button
            class="m-2"
            size="small"
            type="primary"
            @click="emits('filterCommit', { typeArr: typeValue, switchArr: switchValue })"
        >
            查询
        </el-button>
    </div>
</template>

<script setup lang="ts">
    import type { FilterItem } from "@/types/common";
    import type { PropType } from "vue";
    import { ref, defineProps, defineEmits } from "vue";

    const props = defineProps({
        filterItem:{ type:Object as PropType<FilterItem>, required: true },
    });
    

    const emits = defineEmits(["filterCommit"]);

    //大类选项
    const typeValue: Ref<string[]> = ref<string[]>([]);

    //小类选项
    const switchValue: Ref<boolean[]> = ref<boolean[]>([true, true]);
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .filter-bar{
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(100px, 1fr)); // 自动适应列数，每列最小宽度为 100px
        grid-template-rows: 1fr; // 只有一行
        gap: 20px; // 网格项之间的间距
        justify-content: start;
        justify-items: start;

        .el-select{
            box-sizing: border-box;
            width: 100%;
            margin-right: 15px;

            ::placeholder{
                color: #707070;
            }
        }
        .el-popper{
            box-sizing: border-box;
            width: 100%;
        }
        .el-switch{
            margin-right: 15px;
            width: 100%;
        }
        .el-button{
            @include fixedWidth(80px);
        }
    }
</style>