<template>
    <div class="videoUpload">
        <el-upload
            class="upload-demo"
            drag
            action=""
            multiple>
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
                <el-space direction="vertical">
                    <el-text class="mx-1">拖拽到此处</el-text>
                    <el-button type="primary">点击上传</el-button>
                </el-space>
            </div>
            <template #tip>
                <div class="el-upload__tip">
                    <el-row justify="center">
                        <el-text>仅支持mp4格式</el-text>
                    </el-row>
                </div>
                    <el-row justify="center">
                        <el-text>上传视频，即表示您已同意</el-text>
                        <el-link type="primary" href="">《用户协议》</el-link>
                        <el-text>与</el-text>
                        <el-link type="primary" href="">《隐私政策》</el-link>
                    </el-row>
                
            </template>
        </el-upload>
    </div>
</template>

<script lang="ts" setup>
    import { UploadFilled } from '@element-plus/icons-vue'
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .videoUpload{
        @include fullInParent();
        @include flexCenter();
        background-color: rgb(230,234,238);
        
        &::v-deep(.upload-demo){
            width: 70%;
            max-width: 700px;
        }
    }

</style>