<template>
    <div class="rtc">
        <div class="dplayer" ref="dplayer"></div>
    </div>
</template>

<script setup lang="ts">
    import DPlayer from 'dplayer';

    const dplayer:Ref<HTMLElement | undefined> = ref<HTMLElement | undefined>();
    
    onMounted(()=>{
        const dp = new DPlayer({
            container: dplayer.value,
            video: {
                url: 'demo.mp4',
            },
        });
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .rtc{

    }
</style>