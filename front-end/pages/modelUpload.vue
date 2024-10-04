<template>
    <div class="videoUpload">
        <transition-group name="v">
            <model-upload-entity
                key="0"
                v-show="!hadUpload"
                @upload-start="hadUpload=true"
                @upload-progress="progressChange"
            >
            </model-upload-entity>

            <div
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
                    <div>
                        <model-upload-cover
                            @upload-start="changeCover"
                        ></model-upload-cover>
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
                            v-model="category.oriLan" 
                            placeholder="原始语言" 
                            clearable>
                            <el-option
                                v-for="item in lanOpts"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            />
                        </el-select>

                        <el-select
                            v-model="category.tarLan" 
                            placeholder="目标语言" 
                            clearable>
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
            </div>
        </transition-group>
    </div>
</template>

<script lang="ts" setup>
    import type { Category } from "@/types/model";

    const progress = ref<string>('0.00%');
    const hadUpload = ref<boolean>(false);
    const fileName = ref<string>("");

    function progressChange(pgs:any):void{
        progress.value = pgs;
    }
    
    const lanOpts = [
        { value: 'cn', label: '中文' },
        { value: 'en', label: '英语' },
        { value: 'jp', label: '日语' },
    ];

    const cover = ref<Blob>();//封面
    const title = ref<string>("");//标题
    const content = ref<string>("");//简介
    const category = reactive<Category>(//标签
        {
            tarLan:"", 
            oriLan:""
        }
    );
    
    function changeCover(coverFile:any):void{//封面上传回调
        cover.value = coverFile;
        return;
    }
    
    // 校验输入标题
    function validateTitle(title:string):boolean{
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
    function validateCover(cover:Blob|undefined):boolean{
        if (!cover) {
            ElMessage.error('请输入封面');
            return false;
        }
        
        return true;
    }
    
    // 校验输入简介
    function validateContent(content:string):boolean{
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
    
    // 校验输入标签
    function validateCategory(category:Category):boolean{
        let oriLanFlag:boolean = false;
        let tarLanFlag:boolean = false;
        
        lanOpts.forEach(item=>{
            if(item.value == category.oriLan){
                oriLanFlag = true;
            }
            if(item.value == category.tarLan){
                tarLanFlag = true;
            }
        })

        if(!oriLanFlag){
            ElMessage.error('请选择源语言');
            return false;
        }
        else if(!tarLanFlag){
            ElMessage.error('请选择目标语言');
            return false;
        }
        
        return true;
    }
    
    const sendModelInfo = async ():Promise<void>=>{
        if(!validateCover(cover.value)){
            return;
        }
        else if(!validateTitle(title.value)){
            return;
        }
        else if(!validateContent(title.value)){
            return;
        }
        else if(!validateCategory(category)){
            return;
        };
        
        uploadModelInfo({
            cover: cover.value,
            title: title.value,
            content: content.value,
            category: category
        });
    }
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .videoUpload{
        @include fullInParent();
        @include flexCenter();
        background-color: rgb(230,234,238);

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
        transition: all .7s ease;
    }
    .v-enter-to{
        opacity: 1;
    }
</style>