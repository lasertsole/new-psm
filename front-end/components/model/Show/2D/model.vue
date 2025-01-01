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
        const shadowRoot:any = entity.value!.attachShadow({ mode: 'closed' });
        shadowRoot.id = 'oml2d-stage';
        const entityContainer:ChildNode  = entityContainerTemplate.value!.firstChild!
        shadowRoot.appendChild(entityContainer);
        
        const oh_my_live2d = () => import('oh-my-live2d');
        const { loadOml2d } = await oh_my_live2d();
        loadOml2d({
            parentElement: entityContainer as HTMLElement,
            sayHello: false,
            mobileDisplay: true,
            models: [
                {
                    path: props.entityUrl,
                    scale: 0.08,
                    volume: 1,
                    stageStyle: {
                        transform: undefined,
                        position: undefined,
                    }
                }
            ]
        });
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;

    .entity{
        display: inline-block;
        @include common.fullInParent();
    };
</style>