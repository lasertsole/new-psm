<template>
    <div class="absolute top-1/2 right-0 transform -translate-y-1/2 z-10 bg-white rounded-tl-md rounded-bl-md bg-opacity-50 cursor-pointer">
        <div class="mt-2 mb-2 ml-2 mr-4 w-14 h-14 bg-[url('/icons/phone.svg')] bg-cover bg-center"
            @click="dialogVisible = true"
        >
            <div class="absolute w-5 h-5 leading-5 rounded-full bg-red-500 top-2 right-4 font-bold text-white flex items-center justify-center">
                1
            </div>
        </div>
    </div>
    <el-dialog
        class="dialog"
        v-model="dialogVisible"
        align-center
        title="请选择要加入的房间"
        width="500"
        append-to-body
    ></el-dialog>
</template>

<script lang="ts" setup>
    const dialogVisible:Ref<boolean> = ref<boolean>(false);
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

</style>