<template>
    <div ref="demo" class="model2DShow"></div>
    <div ref="demo1" class="model2DShow"></div>
</template>

<script lang="ts" setup>
    const demo = ref<HTMLElement>();
    const demo1 = ref<HTMLElement>();
    
    onMounted(async () => {
        const oh_my_live2d = () => import('oh-my-live2d');
        const { loadOml2d } = await oh_my_live2d();
        loadOml2d({
            parentElement: demo.value,
            sayHello: false,
            models: [
                {
                    path: 'https://model.oml2d.com/HK416-1-normal/model.json',
                    position: [0, 60],
                    scale: 0.08,
                    volume: 1,
                    stageStyle: {
                        height: 450
                    }
                }
            ]
        });
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    .model2DShow {
        display: inline-block;
        @include fullInParent();
    }
</style>