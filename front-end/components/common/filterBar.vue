<template>
    <div class="filter-bar">
        <template v-for="(item,index) in filterItem.selectList" :key="index">
            <el-select v-model="typeValue[index]" class="m-2" :placeholder="item&&item[0].label" size="small">
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
    let selectListLength:number = props.filterItem.selectList?props.filterItem.selectList.length:0;
    const typeValue = ref<number[]>([]);
    for(let i = 0;i < selectListLength; i++){
        typeValue.value[i] = 0;
    }
    

    //小类选项
    let switchListLength:number = props.filterItem.switchList?props.filterItem.switchList.length:0;
    const switchValue = ref<boolean[]>([]);
        for(let i = 0;i < switchListLength; i++){
            switchValue.value[i] = false;
    }

    //当上述条件变化时向服务器发出请求
    watch([typeValue, switchValue],(newVal, oldVal)=>{
        emits("changeClassifyOption", newVal);
    });
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