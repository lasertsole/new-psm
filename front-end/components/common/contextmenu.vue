<template>
    <ul
        v-if="isVisible"
        :style="{ top: `${top}px`, left: `${left}px` }"
        class="context-menu"
    >
        <template v-for="(item, index) in contextMenuOptions" :key="index">
            <li @click="item.callback()">{{ item.text }}</li>
        </template>
    </ul>
</template>

<script setup lang="ts">
    import type { PropType } from "vue";
    import type { ContextMenuOptions } from "@/types/common";

    const props = defineProps({
        isVisible: { type:Boolean, required: true },
        top: { type:Number, required: true },
        left: { type:Number, required: true },
        contextMenuOptions: { type: Array as PropType<ContextMenuOptions[]>, required: true }
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";


</style>