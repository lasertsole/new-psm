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
                        <el-text>仅支持obj格式</el-text>
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
    import type { UploadProps, UploadRawFile } from 'element-plus';
    import { UploadFilled } from '@element-plus/icons-vue'

    const emits = defineEmits(['upload-start', 'upload-progress', "file-url"]);

    const hadUpload = ref<boolean>(false);
    const targetFilePath = ref<string>("");

    const requestModel = async (params:any):Promise<void>=>{//替换掉原本的xhr请求
        hadUpload.value = true;
        await uploadModel3d(params.file
        , (progress:string):void=>{
            emits('upload-progress', progress);
        }
        , targetFilePath);
        return;
    }

    const beforeModelUpload: UploadProps['beforeUpload'] = (rawFile:UploadRawFile):boolean => {//上传视频校验
        let typeArr=['obj'];//能接收的视频文件类型
        if (typeArr.indexOf(rawFile.name.split(".")[1]) < 0) {
            ElMessage.error('请导入模型类型文件');
            return false;
        } 

        if (rawFile.size / 1024 / 1024 /1024 > 2) {
            ElMessage.error('模型大小不能超过2GB');
            return false;
        }

        // 生成 File URL
        const reader = new FileReader();
        reader.onload = (e) => {
            const fileUrl = URL.createObjectURL(rawFile);
            emits('file-url', fileUrl); // 传递 Blob URL 给父组件
            emits("upload-start", rawFile.name);
        };
        reader.readAsDataURL(rawFile); // 或者使用 readAsArrayBuffer

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