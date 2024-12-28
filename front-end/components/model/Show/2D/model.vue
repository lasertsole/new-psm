<template>
    <div ref="entity" class="entity"></div>
    <template ref="entityContainerTemplate">
        <div style="height: 100%; width: 100%; display: flex; justify-content: center; align-items: center; position: relative;"></div>
    </template>
</template>

<script lang="ts" setup>
    const entity = ref<HTMLElement>();
    const entityContainerTemplate = ref<HTMLElement>();

    const props = defineProps({
        entityUrl: {type: String, required: true},
    });

    onMounted(async () => {
        // 创建 Shadow DOM
        const shadowRoot = entity.value.attachShadow({ mode: 'closed' });
        shadowRoot.id = 'oml2d-stage';
        const entityContainer:HTMLElement = entityContainerTemplate.value.firstChild!
        shadowRoot.appendChild(entityContainer);
        
        const oh_my_live2d = () => import('oh-my-live2d');
        const { loadOml2d } = await oh_my_live2d();
        loadOml2d({
            parentElement: entityContainer,
            sayHello: false,
            mobileDisplay: true,
            models: [
                {
                    path: props.entityUrl,
                    scale: 0.08,
                    volume: 1,
                    stageStyle: {
                        transform: null,
                        position: null,
                    }
                }
            ]
        });
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .entity{
        display: inline-block;
        @include fullInParent();
    };
</style>