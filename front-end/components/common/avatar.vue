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
    @use "@/common.scss" as common;

    .avatar {
        @include common.fullInParent;

        :deep(.el-avatar){
            @include common.fullInParent;

            background-color: transparent;

            img{
                @include common.fullInParent;
            }
        }
    }
</style>