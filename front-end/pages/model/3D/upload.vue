<template>
    <div class="videoUpload">
        <transition-group name="v">
            <model-upload-entity
                key="0"
                v-show="!hadUpload"
                @upload-start="uploadStart"
                @upload-progress="progressChange"
                @file-url="processFileUrl"
            >
            </model-upload-entity>

            <el-main
                v-loading="loading"
                key="1"
                v-show="hadUpload"
                class="upload-detail"
            >
                <div class="videoProgress">
                    <div class="icon"></div>
                    <div class="info">
                        <div class="detail">
                            <span class="name">{{ fileName }}</span>
                            <span class="pencentage">{{ progress }}</span>
                        </div>
                        <div class="progressBar">
                            <div :class="{progress:true, success:progress=='100.00%'}"></div>
                        </div>
                    </div>
                </div>
                
                <div class="cover">
                    <span>封面</span>
                    <div class="modelEntity"
                        v-if="entityLocalUrl"
                        title="双击全屏预览,进入预览后按回车截图"
                    >
                        <ModelShow3DModel
                            :entityUrl="entityLocalUrl"
                            :isSnapshot="true"
                            @snapShotBlob="processSnapShot"
                        ></ModelShow3DModel>
                    </div>

                    <div class="coverEntity">
                        <model-upload-cover
                            ref="coverUploadDom"
                        ></model-upload-cover>
                    </div>
                </div>

                <div class="visible">
                    <span>可见性</span>
                    <div>
                        <el-select
                            v-model="visible" 
                            placeholder="请选择作品可见性" 
                            clearable
                            placement="top"
                        >
                            <el-option
                                v-for="item in visibleOpts"
                                :key="item[0]"
                                :label="item[0]"
                                :value="item[1]"
                            />
                        </el-select>
                    </div>
                </div>
                
                <div class="title">
                    <span>标题</span>
                    <el-input v-model="title" type="text" placeholder="请输入视频标题" maxlength="20" show-word-limit/>
                </div>
                
                <div class="tag">
                    <span>标签</span>
                    <div>
                        <el-select
                            v-model="style" 
                            placeholder="风格" 
                            clearable
                            placement="top"
                        >
                            <el-option
                                v-for="item in styleOpts"
                                :key="item[0]"
                                :label="item[0]"
                                :value="item[1]"
                            />
                        </el-select>

                        <el-select
                            v-model="type" 
                            placeholder="类型" 
                            clearable
                            placement="top"
                        >
                            <el-option
                                v-for="item in typeOpts"
                                :key="item[0]"
                                :label="item[0]"
                                :value="item[1]"
                            />
                        </el-select>
                    </div>
                </div>
                
                <div class="profile">
                    <span>简介</span>
                    <div>
                        <el-input
                            v-model="content"
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
                    <el-button type="primary" @click="sendModelInfo">发送</el-button>
                </div>
            </el-main>
        </transition-group>
    </div>
</template>

<script lang="ts" setup>
    import { VisibleEnum } from "@/enums/visible.d";
    import { StyleEnum, TypeEnum } from "@/enums/model3d.d";
    import ModelUploadCover from '@/components/model/Upload/uploadCover.vue';

    const loading:Ref<boolean> = ref<boolean>(false);
    const progress:Ref<string> = ref<string>('0.00%');
    const hadUpload:Ref<boolean> = ref<boolean>(false);
    const fileName:Ref<string> = ref<string>("");

    const coverUploadDom: Ref<InstanceType<typeof ModelUploadCover> | undefined> = ref<InstanceType<typeof ModelUploadCover> | undefined>();
    function processSnapShot(snapshot:File) {
        coverUploadDom.value?.changeCover(snapshot);
        cover.value = snapshot;
    }

    // 开始上传模型文件回调
    function uploadStart(fln:string):void {
        fileName.value = fln;
        hadUpload.value = true;
    }
    
    // 进度条回调
    function progressChange(pgs:string):void {
        progress.value = pgs;
    }

    const entityLocalUrl: Ref<string|undefined> = ref<string>();
    function processFileUrl(fileUrl:string):void {
        entityLocalUrl.value = fileUrl;
    }
    
    // 可见性列表
    const visibleOpts = Object.entries(VisibleEnum).filter((item)=>{ return !/^\d+$/.test(item[0]) });
    // 样式标签列表
    const styleOpts = Object.entries(StyleEnum);
    // 类型标签列表
    const typeOpts = Object.entries(TypeEnum);

    const cover = ref<File>();//封面
    const visible = ref<string>("");//可见性
    const title = ref<string>("");//标题
    const content = ref<string>("");//简介
    const style = ref<string>("");//模型风格
    const type = ref<string>("");// 模型类型
    
    // 校验输入标题
    function validateTitle(title:string):boolean {
        const regex = new RegExp("^[\u4e00-\u9fa5a-zA-Z0-9_]+$");

        if (title == '') {
            ElMessage.error('请输入标题');
            return false;
        }
        else if (title.length > 20) {
            ElMessage.error('标题超过20个字符');
            return false;
        }
        else if(!regex.test(title)){
            ElMessage.error('标题含有非法字符');
            return false;
        }
        
        return true;
    }

    // 校验输入封面
    function validateCover(cover:Blob|undefined):boolean {
        if (!cover) {
            ElMessage.error('请输入封面');
            return false;
        }
        
        return true;
    }
    
    function validateVisible(visible:string|undefined):boolean {
        if (visible === '') {
            ElMessage.error('请选择可见性');
            return false;
        }
        
        return true;
    }
    
    // 校验输入简介
    function validateContent(content:string):boolean {
        const regex = new RegExp("^[\u4e00-\u9fa5a-zA-Z0-9_]+$");

        if (content == '') {
            ElMessage.error('请输入简介');
            return false;
        }
        else if (content.length > 20) {
            ElMessage.error('简介超过255个字符');
            return false;
        }
        else if(!regex.test(content)){
            ElMessage.error('简介含有非法字符');
            return false;
        }
        
        return true;
    }
    
    // 校验输入样式标签
    function validateCategory(style:string, type:string):boolean {
        let styleFlag:boolean = false;
        let typeFlag:boolean = false;
        
        styleOpts.forEach(item=>{
            if(item[1] == style){
                styleFlag = true;
            }
        });

        typeOpts.forEach(item=>{
            if(item[1] == type){
                typeFlag = true;
            }
        });

        if(!styleFlag){
            ElMessage.error('请选择样式');
            return false;
        }
        else if(!typeFlag){
            ElMessage.error('请选择类型');
            return false;
        }
        
        return true;
    }
    
    const sendModelInfo = debounce(async ():Promise<void>=> {
        if(!validateCover(cover.value)){
            return;
        }
        else if(!validateVisible(visible.value)){
            return;
        }
        else if(!validateTitle(title.value)){
            return;
        }
        else if(!validateContent(title.value)){
            return;
        }
        else if(!validateCategory(style.value, type.value)) {
            return;
        }else if(progress.value!="100.00%"){
            ElMessage.error('请等待文件上传完成');
            return;
        };
        
        loading.value = true;
        uploadModel3dInfo({
            cover: cover.value,
            title: title.value,
            content: content.value,
            style: style.value,
            type: type.value,
            visible: visible.value.toString()
        }).finally(()=>{loading.value = false});
    }, 1000);

    definePageMeta({
        name: 'model-3D-upload'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .videoUpload{
        @include fullInParent();
        @include flexCenter();
        background-color: rgb(230,234,238);

        .upload-detail{
            @include fixedRoundedRectangle(80%, 95%, 20px);
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
                    @include fullImg("/icons/model.svg");
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
                @include fullWidth();
                display: flex;
                flex-direction: row;
                justify-content: flex-start;
                align-items: center;
                
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

                    &:last-child{
                        flex-grow: 1;
                    }
                }
                
                &:not(:first-child){
                    margin-top: 20px;
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

            .visible{
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

            .cover{
                .modelEntity{
                    @include fixedRetangle(220px, 180px);
                    cursor: pointer;
                }

                .coverEntity{
                    margin-left: 10px;
                }
            }
        }
    }

    //transition出入场动画
    .v-enter-from{
        opacity: 0;
    }
    .v-enter-active{
        transition: all .7s ease;
    }
    .v-enter-to{
        opacity: 1;
    }
</style>