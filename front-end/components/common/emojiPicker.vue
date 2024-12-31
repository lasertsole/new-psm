<template>
    <div ref="pickerRef"></div>
</template>

<script setup lang="ts">
    import data from '@emoji-mart/data';
    import { Picker } from 'emoji-mart';

    const pickerRef = ref<HTMLElement>();
    onMounted(async() => {
        const emojiModule = await import('emoji-mart');
        const emojiPicker = new emojiModule.Picker({data});
        pickerRef.value!.appendChild(emojiPicker);
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;
</style>