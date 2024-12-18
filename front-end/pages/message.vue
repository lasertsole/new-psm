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
                        <template v-for="(item, index) in DMContactsItems" :key="item.id">
                            <MessageContactsBox
                                :tgtUserId="item.tgtUserId!"
                                :avatar="item.avatar!"
                                :lastMessage="item.lastMessage!"
                                :unread="item.unread!"
                                :isMuted="item.isMuted!"
                                :isGroup="item.isGroup!"
                                :isSeleted="index==nowDMContactsIndex"
                                :index="index"
                                :callBack="scrollToBottom"
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
                            {{ DMContactsItems[nowDMContactsIndex]?.name }}
                        </span>
                    </div>
                    <div class="messageList" ref="messageList">
                        <div class="containerBox" v-show="nowDMContactsIndex>=0">
                            <div class="topGap"></div>
                            <template v-for="(item, index) in DMContactsItems[nowDMContactsIndex]?.messageItems" :key="index">
                                <messageBox
                                    :avatar="DMContactsItems[nowDMContactsIndex].avatar!"
                                    :name="DMContactsItems[nowDMContactsIndex].name!"
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
                        <div class="toolBar" v-show="nowDMContactsIndex>=0">
                            <i class="rtc" title="音视频通话" @click="sendRTCRequest"></i>
                        </div>

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
    import type { ContactsItem } from '@/types/chat';

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
    };

    // 获取聊天记录栏对象
    const messageList:Ref<HTMLElement | undefined> = ref<HTMLElement | undefined>();
    // 聊天记录列表滚动到底部
    function scrollToBottom():void {
        messageList.value!.scrollTop=messageList.value!.scrollHeight;
    };
        
    let DMServiceInstance: DMService | null = null;//DM服务实例
    let RTCServiceInstance: RTCService | null = null;//RTC服务实例

    // 发送信息
    const send = debounce(():void=>{
        if(!validateMessage(message.value)) { return; };
           
            DMServiceInstance!.sendMessage(message.value).then(()=>{
                scrollToBottom();// 发送完消息后滑动到底部
            });
        message.value="";
    });

    onMounted(()=>{
        if(nowDMContactsIndex.value>=0) { // 如果挂载完成时nowDMContactsIndex.value不为-1，说明是通过toDM事件跳转到本页面的
            nextTick(()=>{// 这时需要把聊天记录列表滑动到底部
                scrollToBottom();
            });
        };
    });

    onActivated(debounce(()=>{
        if(!userInfo.isLogin) {
            DMServiceInstance = null;
            RTCServiceInstance = null;
            return;
        };
        DMServiceInstance=DMService.getInstance();

        // 如果RTCServiceInstance不存在，则获取实例，以及添加链接建立事件的回调
        if(!RTCServiceInstance){
            RTCServiceInstance = RTCService.getInstance();// 获取实例
        };
    }, 1000));

    const mask:Ref<boolean> = ref<boolean>(false);// 控制显示遮罩
    const showRTCWindow:Ref<boolean> = ref<boolean>(false); // 控制显示RTC播放器窗口
    const sendRTCRequest = debounce(async ():Promise<void>=> {
        await new Promise((resolve)=>{
            RTCServiceInstance!.createRoom(
                ()=>{
                    resolve(true);
                },
                ()=>{
                    throw new Error("crash when create Room");
                }
            );
        }).then((res)=>{
            const contactsItem:ContactsItem = DMContactsItems[nowDMContactsIndex.value];
            RTCServiceInstance!.inviteJoinRoom(
                contactsItem.tgtUserId,
                contactsItem.name!,
                ()=>{
                    return res;
                },
                ()=>{
                    throw new Error("crash when invite peer");
                }
            );
        }).catch((e)=>{
            ElMessage.warning(e);
        }).finally(()=>{
            mask.value=false;
        });
    }, 1000);

    const openRTCWindow = debounce(()=>{
        showRTCWindow.value=true;
    }, 1000);

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

        .container {
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

            >div {
                box-shadow: 0 2px 4px 0 rgba(121, 146, 185, 0.54);
                border-radius: 4px;
                overflow: hidden;
            }

            $titleBoxHeight: 42px;
            .titleBox {
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
            .chatBox {
                @include fullWidth();
                margin-top: $chatBoxMarginTop;
                height: calc(100% - $titleBoxHeight - $chatBoxMarginTop);
                display: flex;

                >div {
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

                .left {
                    @include fixedWidth(220px);
                    &{
                        margin-right: 1.6px;
                        display: flex;
                        flex-direction: column;
                    }

                    .title {
                        color: #666666;
                        font-size: 12px;
                        padding-left: 24px;
                        display: flex;
                        align-items: center;
                    }

                    .contactList {
                        flex-grow: 1;
                        @include scrollBar(8px);
                    }
                }
                
                .right {
                    flex-grow: 1;
                    display: flex;
                    flex-direction: column;

                    .title {
                        @include flexCenter;
                        color: #333333;
                        font-size: 14px;
                    }

                    .messageList {
                        @include scrollBar(8px);
                        flex-grow: 1;
                        margin-bottom: 0.8px;
                        
                        .topGap {
                            @include fixedHeight(22px);
                            @include fullWidth();
                        }
                        
                        .containerBox {
                            @include fullInParent;
                        }
                    }

                    $padding: 16px;
                    $buttonWidth: 88px;
                    $buttonHeight: 30px;
                    $countHeight: 14px;
                    $toolBarHeight: 30px;
                    .sendBox {
                        @include fixedHeight(162px);
                        margin-top: 0.8px;
                        position: relative;
                        z-index: 1;

                        .toolBar{
                            padding: 0px $padding;
                            @include fullWidth;
                            @include fixedHeight($toolBarHeight);
                            position: absolute;
                            z-index: 2;
                            display: flex;
                            align-items: center;
                            i{
                                @include fixedSquare(20px);
                                @include fullImg("icons/phone.svg");
                                cursor: pointer;
                            }
                        }
                        
                        &::v-deep(.el-textarea) {
                            @include fullInParent();

                            *{
                                background-color: rgba(14, 12, 12, 0);// 透明背景
                            }
                            
                            .el-textarea__inner {
                                @include scrollBar(8px);
                                
                                &{
                                    width: 100% !important;
                                    height: 100% !important;
                                    padding: $toolBarHeight $padding $padding;
                                }
                            }

                            .el-input__count {
                                @include fixedHeight($countHeight);
                                right: $padding + $buttonWidth + 15px;
                                bottom: $padding + math.div(($buttonHeight - $countHeight), 2);
                            }
                        }

                        button {
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