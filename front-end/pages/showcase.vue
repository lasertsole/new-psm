<template>
    <input type="text" v-model="str"/>
    <hr>
    <div v-html="str">
    </div>
</template>

<script setup lang="ts">
    const str = ref<string>("");
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

</style>