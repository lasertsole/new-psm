<template>
    <div class="UploadEntity">
        <el-upload
            v-if="!hadUpload"
            class="upload-base"
            drag
            :http-request="requestModel"
            :before-upload="beforeModelUpload"
            >
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
    import type { UploadProps } from 'element-plus';
    import { UploadFilled } from '@element-plus/icons-vue'

    const emits = defineEmits(['upload-start','upload-progress']);

    const hadUpload = ref<boolean>(false);
    const fileName = ref<string>("");
    const targetFilePath = ref<string>("");

    const requestModel = async (params:any):Promise<void>=>{//替换掉原本的xhr请求
        hadUpload.value = true;
        await uploadModel(params.file
        , (progress:string):void=>{
            emits('upload-progress', progress);
        }
        , targetFilePath);
        return;
    }

    const beforeModelUpload: UploadProps['beforeUpload'] = (rawFile):boolean => {//上传视频校验
        fileName.value = rawFile.name;
        let typeArr=['video/mp4'];//能接收的视频文件类型
        if (typeArr.indexOf(rawFile.type)<0) {
            ElMessage.error('请导入视频类型文件');
            return false;
        } 

        if (rawFile.size / 1024 / 1024 /1024 > 2) {
            ElMessage.error('视频大小不能超过2GB');
            return false;
        }

        emits("upload-start");
        return true;
    }
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .UploadEntity{
        @include fullInParent();
        @include flexCenter();
        
        &::v-deep(.upload-base){
            width: 70%;
            max-width: 700px;
        }
    }
</style>