<template>
    <div class="filter-bar">
        <template v-for="(item,index) in filterItem.selectList" :key="index">
            <el-select v-model="typeValue[index]" class="m-2" placeholder="no select" size="small">
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
            @click="emits('changeClassifyOption', [])"
        >
            查询
        </el-button>
    </div>
</template>

<script setup lang="ts">
    import type { FilterItem } from "@/types/common";
    import type { PropType } from "vue";
    import { ref, defineProps, defineEmits, watch } from "vue";

    const props = defineProps({
        filterItem:{ type:Object as PropType<FilterItem>, required: true },
    });
    

    const emits = defineEmits(["changeClassifyOption"]);

    //大类选项
    const typeValue = ref<string[]>([]);

    

    //小类选项
    const switchValue = ref<boolean[]>([]);
</script>

<style lang="scss" scoped>
    .filter-bar{
        .el-select{
            box-sizing: border-box;
            width: 108px;
            margin-right: 15px;

            ::placeholder{
                color: #707070;
            }
        }
        .el-popper{
            box-sizing: border-box;
        }
        .el-switch{
            margin-right: 15px;
        }
    }
</style>