<template>
    <div class="avatar">
        <el-avatar :src="src" @error="errorHandler" loading="lazy">
            <img
                :src="useRuntimeConfig().public.defaultAvatar"
                loading="lazy"
            />
        </el-avatar>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        src: String
    });
    const errorHandler = () => true
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .avatar {
        @include fullInParent;

        :deep(.el-avatar){
            @include fullInParent;

            background-color: transparent;

            img{
                @include fullInParent;
            }
        }
    }
</style>