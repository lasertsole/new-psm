<template>
    <div
        class="avatar-uploader"
    >
        <el-upload
            v-loading="loading"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :http-request="request"
        >
            <CommonAvatar
                class="avatar"
                :src="userInfo.avatar"
            >
            </CommonAvatar>
            
            <div class="mock">
                <div class="rowLine"></div>
                <div class="columnLine"></div>
            </div>
        </el-upload>
    </div>
</template>
  
<script lang="ts" setup>
    import { ElMessage } from 'element-plus';
    import type { UploadProps } from 'element-plus';

    const loading:Ref<boolean> = ref<boolean>(false); // 是否正在上传中

    const handleAvatarSuccess: UploadProps['onSuccess'] = (response,uploadFile) => {
        // imageUrl.value = URL.createObjectURL(uploadFile.raw!);
    };

    const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile):boolean => {//上传图片校验
        let typeArr=['image/jpeg', 'image/webp', 'image/png', 'svg+xml'];//能接收的图片文件类型
        if (typeArr.indexOf(rawFile.type)<0) {
            ElMessage.error('请输入图片类型文件');
            return false;
        } 

        if (rawFile.size / 1024 / 1024 > 5) {
            ElMessage.error('图片大小不能超过5MB');
            return false;
        }

        return true;
    }

    async function request(params:any):Promise<void>{//替换掉原本的xhr请求
        loading.value = true;
        updateAvatar(params.file).finally(()=>loading.value = false);
    }
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;

    .avatar-uploader{
        @include common.fullInParentCircle();
        border: 1px dashed #00a8e9;
        overflow: hidden;
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
        position: relative;


        .avatar{
            @include common.fullInParent();
            position: relative;
            z-index:1;
            background-color: white;
        }

        .mock{
            @include common.fullInParent();
            position: absolute;
            top:0px;
            left:0px;
            z-index:2;
            background-color: black;
            opacity: 0;
            transition: opacity .3s ease;
            display: flex;
            justify-content: center;
            align-items: center;

            &:hover{
                opacity: 0.3;
            }

            .rowLine{
                background-color:white;
                @include common.fixedRetangle(3px, 17px);
                position: absolute;
                z-index: 1;
            }

            .columnLine{
                background-color:white;
                @include common.fixedRetangle(17px, 3px);
                position: absolute;
                z-index: 2;
            }
        }
    }
</style>