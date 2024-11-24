<template>
    <div class="message">
        <el-main class="container" v-loading="!isInitDM">
            
            <div class="titleBox">
                <span>我的消息</span>
            </div>

            <div class="chatBox">
                
                <div class="left">
                    <div class="title">近期消息</div>
                    <div class="contactList">
                        <template v-for="(item, index) in contactsItems" :key="item.id">
                            <MessageContactsBox
                                :tgtUserId="item.tgtUserId!"
                                :avatar="item.avatar!"
                                :lastMessage="item.lastMessage!"
                                :unread="item.unread!"
                                :isMuted="item.isMuted!"
                                :isGroup="item.isGroup!"
                                :isSeleted="index==nowDMContactsIndex"
                                :index="index"
                            >
                                <template #lastTime>{{ formatToLocalTime(item.lastTime) }}</template>
                                <template #name>{{ item.name }}</template>
                                <template #lastMessage>{{ item.lastMessage }}</template>
                            </MessageContactsBox>
                        </template>
                    </div>
                </div>
                
                <div class="right">
                    <div class="title">
                        <span v-show="nowDMContactsIndex>=0">
                            {{ contactsItems[nowDMContactsIndex]?.name }}
                        </span>
                    </div>
                    <div class="messageList">
                        <div class="containerBox" v-show="nowDMContactsIndex>=0">
                            <div class="topGap"></div>
                            <template v-for="(item, index) in contactsItems[nowDMContactsIndex]?.messageItems" :key="index">
                                <messageBox
                                    :avatar="contactsItems[nowDMContactsIndex].avatar!"
                                    :name="contactsItems[nowDMContactsIndex].name!"
                                    :type="item.type!"
                                    :srcUserId="item.srcUserId!"
                                    :tgtUserId="item.tgtUserId!"
                                    :timestamp="item.timestamp!"
                                    :isDeleted="item.isDeleted!"
                                    :status="item.status!"
                                    
                                >
                                    <template #text>
                                        {{ item.content }}
                                    </template>
                                </messageBox>
                            </template>
                        </div>
                    </div>
                    <div class="sendBox">
                        <el-input
                            v-show="nowDMContactsIndex>=0"
                            v-model="message"
                            type="textarea"
                            placeholder="请输入文字"
                            maxlength="255"
                            show-word-limit
                            resize="none"
                        />
                        <el-button
                            plain
                            type="primary"
                            @click="send"
                            v-show="nowDMContactsIndex>=0"
                        >
                            发送
                        </el-button>
                    </div>
                </div>
                
            </div>
            
        </el-main>
    </div>
</template>

<script lang="ts" setup>
    // 获取当前路由对象
    const route = useRoute();

    const message:Ref<string> = ref<string>("");

    function validateMessage(content:string):boolean {
        if(content.length==0){
            ElMessage.warning("发送信息不能为空");
            return false;
        }
        else if(content.length>255){
            ElMessage.warning("发送信息不能超过255个字符");
            return false;
        }
        return true;
    }
        
    // 发送信息
    const send = debounce(():void=>{
        if(!validateMessage(message.value))
            return;

            sendMessage(message.value);
        message.value="";
    });

    onMounted(()=>{
    });
        
    onActivated(()=>{
        // 初始化私信
        initDM();
    });

    definePageMeta({
        name: 'message'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .message {
        @include fullInParent();
        background-color: rgba(222, 222, 222, .75);
        display: flex;
        justify-content: center;
        overflow: hidden;

        .container{
            max-width: 980px;
            width: 80%;
            padding: 10px;
            display: flex;
            flex-direction: column;

            @include fixedWidth(80%);
            @include fixedHeight(100%);
            
            @media screen and (max-width: 800px) {
                @include fixedWidth(100%);
            }

            @media screen and (max-width: 700px) {
                padding: 10px 0px;
            }

            >div{
                box-shadow: 0 2px 4px 0 rgba(121, 146, 185, 0.54);
                border-radius: 4px;
                overflow: hidden;
            }

            $titleBoxHeight: 42px;
            .titleBox{
                @include fullWidth();
                @include fixedHeight($titleBoxHeight);
                font-size: 15px;
                color: #666;
                padding: 0px 16px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #f4f5f7;
            }

            $chatBoxMarginTop: 10px;
            .chatBox{
                @include fullWidth();
                margin-top: $chatBoxMarginTop;
                height: calc(100% - $titleBoxHeight - $chatBoxMarginTop);
                display: flex;

                >div{
                    @include fullHeight();

                    >div{
                        background-color: #f4f5f7;
                        overflow: hidden;
                    }

                    >.title{
                        @include fixedHeight(36px);
                        margin-bottom: 1.6px;
                    }
                }

                .left{
                    @include fixedWidth(220px);
                    &{
                        margin-right: 1.6px;
                        display: flex;
                        flex-direction: column;
                    }

                    .title{
                        color: #666666;
                        font-size: 12px;
                        padding-left: 24px;
                        display: flex;
                        align-items: center;
                    }

                    .contactList{
                        flex-grow: 1;
                        @include scrollBar(8px);
                    }
                }
                
                .right{
                    flex-grow: 1;
                    display: flex;
                    flex-direction: column;

                    .title{
                        @include flexCenter;
                        color: #333333;
                        font-size: 14px;
                    }

                    .messageList{
                        @include scrollBar(8px);
                        flex-grow: 1;
                        margin-bottom: 0.8px;
                        
                        .topGap{
                            @include fixedHeight(22px);
                            @include fullWidth();
                        }
                        
                        .containerBox{
                            @include fullInParent;
                        }
                    }

                    $padding: 16px;
                    $buttonWidth: 88px;
                    $buttonHeight: 30px;
                    $countHeight: 14px;
                    
                    .sendBox{
                        @include fixedHeight(162px);
                        margin-top: 0.8px;
                        position: relative;
                        
                        &::v-deep(.el-textarea) {
                            @include fullInParent();

                            *{
                                background-color: rgba(14, 12, 12, 0);// 透明背景
                            }
                            
                            .el-textarea__inner{
                                @include scrollBar(8px);
                                
                                &{
                                    width: 100% !important;
                                    height: 100% !important;
                                    padding: $padding;
                                }
                            }

                            .el-input__count{
                                @include fixedHeight($countHeight);
                                right: $padding + $buttonWidth + 15px;
                                bottom: $padding + math.div(($buttonHeight - $countHeight), 2);
                            }
                        }

                        button{
                            @include fixedWidth($buttonWidth);
                            @include fixedHeight($buttonHeight);
                            position: absolute;
                            right: $padding;
                            bottom: $padding;
                        }
                    }
                }
            }
        }
    }
</style>