<template>
    <div class="videoUpload">
        <transition mode="out-in">
            <el-upload
                v-if="!hadUpload"
                class="upload-base"
                drag
                :http-request="request"
                :before-upload="beforeAvatarUpload"
                :on-success="handleAvatarSuccess"
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

            <div
                v-else
                class="upload-detail"
            >
                <div class="videoProgress">
                    <div class="icon"></div>
                    <div class="info">
                        <div class="detail">
                            <span class="name">{{ fileName }}</span>
                            <span class="pencentage">{{progress}}</span>
                        </div>
                        <div class="progressBar">
                            <div :class="{progress:true, success:progress=='100.00%'}"></div>
                        </div>
                    </div>
                </div>
                <div class="cover">
                    <span>封面</span>
                    <div>
                        <el-upload
                            drag
                        >
                            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                            <div class="el-upload__text">
                                <el-space direction="vertical">
                                    <el-text class="mx-1">拖拽到此处</el-text>
                                    <el-button type="primary">点击上传</el-button>
                                </el-space>
                            </div>
                        </el-upload>
                    </div>
                </div>
                <div class="title">
                    <span>标题</span>
                    <el-input type="text" placeholder="请输入视频标题" />
                </div>
                <div class="tag">
                    <span>标签</span>
                    <div>
                        <el-select placeholder="原始语言" clearable>
                        <el-option
                                v-for="item in lanOpts"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            />
                        </el-select>

                        <el-select placeholder="目标语言" clearable>
                            <el-option
                                v-for="item in lanOpts"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            />
                        </el-select>
                    </div>
                </div>
                <div class="profile">
                    <span>简介</span>
                    <div>
                        <el-input
                            :autosize="{ minRows: 3 }"
                            type="textarea"
                            size="large"
                            placeholder="暂无简介"
                            maxlength="255"
                            show-word-limit
                            resize="none"
                            style="border-radius:5px; overflow: hidden;"
                        />
                    </div>
                </div>
                <div class="send">
                    <el-button type="primary" @click="uploadModelInfo">发送</el-button>
                </div>
            </div>
        </transition>
    </div>
</template>

<script lang="ts" setup>
    import type { UploadProps } from 'element-plus';
    import { UploadFilled } from '@element-plus/icons-vue'

    const progress = ref<string>('0.00%');
    const hadUpload = ref<boolean>(false);
    const fileName = ref<string>("");
    const targetFilePath = ref<string>("");

    const request = async (params:any):Promise<void>=>{//替换掉原本的xhr请求
        hadUpload.value = true;
        await uploadModel(params.file, progress, targetFilePath);
        return;
    }

    const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile):boolean => {//上传视频校验
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

        return true;
    }

    const handleAvatarSuccess: UploadProps['onSuccess'] = (response,uploadFile) => {
        // imageUrl.value = URL.createObjectURL(uploadFile.raw!);
    };

    const lanOpts = [
        { value: '1', label: '1' },
        { value: '2', label: '2' },
        { value: '3', label: '3' },
        { value: '4', label: '4' },
        { value: '5', label: '5' },
    ];
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .videoUpload{
        @include fullInParent();
        @include flexCenter();
        background-color: rgb(230,234,238);
        
        &::v-deep(.upload-base){
            width: 70%;
            max-width: 700px;
        }

        .upload-detail{
            @include fixedRoundedRectangle(80%, 90%, 20px);
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: flex-start;
            padding: 0px 20px;
            background-color: white;

            .videoProgress{
                @include fixedRoundedRectangle(100%, 80px, 10px);
                display: flex;
                padding: 0px 10px;
                background-color: rgb(230,234,238);

                .icon{
                    @include fixedSquare(50px);
                    @include fullImg("/icons/videoFile.svg");
                }

                .info{
                    flex-grow: 1;
                    height: 100%;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    padding: 10px;
                    
                    .detail{
                        display: flex;
                        margin-bottom: 10px;
                        justify-content: space-between;
                        align-items: center;

                        .pencentage{
                            font-size: 12px;
                        }
                    }
                    
                    .progressBar{
                        $progressHeight: 10px;
                        @include fixedRoundedRectangle(100%, $progressHeight, math.div($progressHeight,2));
                        background-color: rgb(203, 207, 211);
                        overflow: hidden;
                        
                        .progress{
                            transition: width .3s linear;
                            height: 100%;
                            $progressWidth: v-bind(progress);
                            width: $progressWidth;
                            border-radius: math.div($progressHeight,2);
                            background-color: rgb(0, 160, 255);
                            
                            &.success{
                                background-color: rgb(0, 255, 0);
                            }
                        }
                    }
                }
            }
            
            >div{
                display: flex;
                flex-direction: row;
                justify-content: flex-start;
                align-items: center;
                @include fullWidth();
                
                >span{
                    @include fixedWidth(80px);
                    display: flex;
                    align-items: center;
                    
                    &::before{
                        content: "*";
                        color: red;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                    }
                }
                
                >div{
                    flex-wrap: wrap;
                    flex-grow: 1;
                }
                
                &:not(:first-child){
                    margin-top: 20px;
                }
            }
            
            .cover{
                &::v-deep(.el-upload-dragger){
                    padding: 10px;
                    width: 50%;
                    min-width: 280px;
                }
            }

            .tag{
                >div{
                    display: flex;
                    
                    &::v-deep(.el-select){
                        $gapWidth: 20px;
                        width: calc(50% - math.div($gapWidth,2));

                        &:not(:last-of-type){
                            margin-right: $gapWidth;
                        }
                    }
                }
            }

            .send{
                display: flex;
                justify-content: center;
            }
        }
    }

    //transition出入场动画
    .v-enter-from{
        opacity: 0;
    }
    .v-enter-active{
        transition: opacity .7s ease;
    }
    .v-enter-to{
        opacity: 1;
    }
    .v-leave-from{
        opacity: 1;
    }
    .v-leave-active{
        transition: opacity .7s ease;
    }
    .v-leave-to{
        opacity: 0;
    }
</style>