<template>
    <div class="searchItem" @click="$emit('click', $event)">
        <div class="searchItem-title">
            <slot name="title">{{title}}</slot>
        </div>
        <div class="searchItem-content">
            <slot name="content">{{content}}</slot>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        Id: {type:String, required: true},
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .searchItem{
        font-size: 14px;
        padding: 5px 0px;
        &:hover{
            background-color: #f5f5f5;
        }

        >div{
            display: flex;
            align-items: center;
            justify-content: flex-start;
        }
    }
</style>