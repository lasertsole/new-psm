<template>
    
</template>

<script setup lang="ts">
    import type { Page } from "@/types/common";
    import type { SubtitlesBox } from "@/types/subtitles";

    const SubtitlesBoxes = ref<SubtitlesBox[] | null>();
    SubtitlesBoxes.value = await getSubtitlesList({ currentPage:1, pageSize:10 });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

</style>