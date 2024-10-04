<template>
  <div class="root">
    <el-upload
      class="cover-uploader"
      :show-file-list="false"
      :http-request="requestCover"
      :before-upload="beforeCoverUpload"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload>
  </div>
</template>
  
<script lang="ts" setup>
  import { ref } from 'vue'
  import { ElMessage } from 'element-plus'
  import { Plus } from '@element-plus/icons-vue'
  
  import type { UploadProps } from 'element-plus'

  const emits = defineEmits(['upload-start']);
  
  const imageUrl = ref('');
  
  const requestCover = async (params:any):Promise<void>=>{//替换掉原本的xhr请求
        imageUrl.value = URL.createObjectURL(params.file);
        emits("upload-start", params.file);
        return;
    };

    const beforeCoverUpload: UploadProps['beforeUpload'] = (rawFile):boolean => {//上传视频校验
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
    };
</script>
  
<style lang="scss" scoped>
  .root{
    &::v-deep(.cover-uploader){
      .avatar {
        width: 178px;
        height: 178px;
        display: block;
      }

      .el-upload {
        border: 1px dashed #dcdfe6;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: .2s;
        
        &:hover {
          border-color: #409eff;
        }
      }

      .el-icon{
        &.avatar-uploader-icon {
          font-size: 28px;
          color: #8c939d;
          width: 178px;
          height: 178px;
          text-align: center;
        }
      }
    }
  }
</style>