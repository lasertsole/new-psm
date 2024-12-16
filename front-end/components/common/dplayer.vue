<template>
    <div class="rtc">
        <div class="dplayer" ref="dplayerDom"></div>
    </div>
</template>

<script setup lang="ts">
    import DPlayer from 'dplayer';

    const dplayerDom:Ref<HTMLElement | undefined> = ref<HTMLElement | undefined>();
    let dp:DPlayer | null = null;
    onMounted(()=>{
        dp = new DPlayer({
            container: dplayerDom.value,
            video: {
                url: 'demo.mp4',
            },
        });
    });

    defineExpose({
        dp
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .rtc{
        position: absolute;
        z-index: 2;
    }
</style>