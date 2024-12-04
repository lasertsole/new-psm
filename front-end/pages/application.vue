<template>
    <ClientOnly>
        <WujieVue
            width="100%"
            height="100%"
            name="webgal"
            url="https://demo.openwebgal.com/"
            sync
            :plugins="[
                LocationReloadPlugin(), 
                EventTargetPlugin(), 
                DocFullScrollPlugin()
            ]"
        ></WujieVue>
    </ClientOnly>
</template>

<script setup lang="ts">
    import { 
        LocationReloadPlugin, 
        EventTargetPlugin, 
        DocFullScrollPlugin 
    } from "wujie-polyfill";
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
</style>