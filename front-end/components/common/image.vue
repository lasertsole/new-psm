<template>
    <div class="image">
      <el-image :src="src" fit="cover" loading="lazy">
        <template #error>
          <div class="image-slot">
            <el-icon><icon-picture /></el-icon>
          </div>
        </template>

        <template #placeholder>
          <div class="image-slot">
            Loading...
          </div>
        </template>
      </el-image>
    </div>
</template>

<script setup lang="ts">
    import { Picture as IconPicture } from '@element-plus/icons-vue';

    const props = defineProps({
        src: String
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .image{
        @include fullInParent;

        :deep(.el-image){
            @include fullInParent;

            background-color: transparent;
        }
    }
</style>