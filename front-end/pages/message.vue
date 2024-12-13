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

        <el-dialog
            v-model="rtcDialogVisible"
            align-center
            title="请选择音频输入"
            width="500"
        >
            <div class="dialog-body">
                <div>
                    <div class="leftBox">视频</div>
                    <div class="rightBox">
                        <template v-for="(item, index) in deviceControl.video">
                            <div :active="item.active?item.active:'undefined'" @click="item.active?(item.active=='true'?item.active='false':item.active='true'):item.active='true'">
                                <div class="icon">
                                    <div></div>
                                    <svg t="1733552418073" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1485" v-if="item.type=='screen'">
                                        <path d="M972.8 768H537.6v128h256v51.2H230.4v-51.2h256v-128H51.2a51.2 51.2 0 0 1-51.2-51.2V128a51.2 51.2 0 0 1 51.2-51.2h921.6a51.2 51.2 0 0 1 51.2 51.2v588.8a51.2 51.2 0 0 1-51.2 51.2z m0-614.4a25.6 25.6 0 0 0-25.6-25.6H76.8a25.6 25.6 0 0 0-25.6 25.6v537.6a25.6 25.6 0 0 0 25.6 25.6h870.4a25.6 25.6 0 0 0 25.6-25.6V153.6z" fill="#1296db" p-id="1486"></path>
                                    </svg>
                                    <svg t="1733562224173" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2565" v-else-if="item.type=='webcam'">
                                        <path d="M907.712 642.592l-2.624-302.592-204.256 145.056 206.88 157.536z m-39.68-354.784a64 64 0 0 1 101.056 51.648l2.624 302.592a64 64 0 0 1-102.752 51.456l-206.912-157.536a64 64 0 0 1 1.728-103.104l204.256-145.056z" fill="#1296db" p-id="2566"></path>
                                        <path d="M144 256a32 32 0 0 0-32 32v417.376a32 32 0 0 0 32 32h456.32a32 32 0 0 0 32-32V288a32 32 0 0 0-32-32H144z m0-64h456.32a96 96 0 0 1 96 96v417.376a96 96 0 0 1-96 96H144a96 96 0 0 1-96-96V288a96 96 0 0 1 96-96z" fill="#1296db" p-id="2567"></path>
                                    </svg>
                                </div>
                                <div class="name">{{ item.name }}</div>
                            </div>
                        </template>
                    </div>
                </div>
                
                <div>
                    <div class="leftBox">音频</div>
                    <div class="rightBox">
                        <template v-for="(item, index) in deviceControl.audio">
                            <div :active="item.active?item.active:'undefined'" @click="item.active?(item.active=='true'?item.active='false':item.active='true'):item.active='true'">
                                <div class="icon">
                                    <div></div>
                                    <svg t="1733564168472" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3711">
                                        <path d="M512 128c35.2 0 64 28.8 64 64v320c0 35.2-28.8 64-64 64s-64-28.8-64-64V192c0-35.2 28.8-64 64-64m0-64c-70.4 0-128 57.6-128 128v320c0 70.4 57.6 128 128 128s128-57.6 128-128V192c0-70.4-57.6-128-128-128z m320 448h-64c0 140.8-115.2 256-256 256S256 652.8 256 512h-64c0 165.6 126.4 302.4 288 318.4V960h64v-129.6c161.6-16 288-152.8 288-318.4z" p-id="3712"></path>
                                    </svg>
                                </div>
                                <div class="name">{{ item.name }}</div>
                            </div>
                        </template>
                    </div>
                </div>
            </div>

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="rtcDialogVisible=false">取消</el-button>
                    <el-button type="primary" @click="openRTCWindow">确认</el-button>
                </div>
            </template>
        </el-dialog>
        <video class="absolute" ref="rtcWindowDom"></video>
        <template v-show="showRTCWindow">
            <!-- <CommonRtc ref="rtcWindowDom"></CommonRtc> -->
        </template>
    </div>
</template>

<script lang="ts" setup>
    import DPlayer from 'dplayer';
    import type { Reactive } from 'vue';
    import type { ContactsItem } from '@/types/chat';
    import type { Devices, RTCSwap } from "@/types/rtc";
    import CommonRtc from '@/components/common/rtc.vue';

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
        
    let DMServiceInstance: DMService;//DM服务实例
    let RTCServiceInstance: RTCService;//RTC服务实例

    // 发送信息
    const send = debounce(():void=>{
        if(!validateMessage(message.value)) { return; };
           
            DMServiceInstance.sendMessage(message.value).then(()=>{
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
        if(!userInfo.isLogin) return;
        DMServiceInstance=DMService.getInstance();

        // 如果RTCServiceInstance不存在，则获取实例，以及添加链接建立事件的回调
        if(!RTCServiceInstance){
            RTCServiceInstance = RTCService.getInstance();// 获取实例
            RTCServiceInstance.onSwapCandidate((remoteSDP: RTCSwap)=>{// 添加链接建立事件的回调
                rtcDialogVisible.value = true;
            });
        };
    }, 1000));

    const rtcWindowDom:Ref<HTMLVideoElement | undefined> = ref<HTMLVideoElement | undefined>();    const mask:Ref<boolean> = ref<boolean>(false);// 控制显示遮罩
    const showRTCWindow:Ref<boolean> = ref<boolean>(false); // 控制显示RTC播放器窗口
    const sendRTCRequest = debounce(()=> {
        if(!rtcWindowDom.value) {
            ElMessage.warning("音频播放器未准备完成。");
            return;
        };
        
        new Promise((resolve)=>{
            RTCServiceInstance.createRoom(
                rtcWindowDom.value!,
                ()=>{
                    resolve(true);
                },
                ()=>{
                    throw new Error("crash when create Room");
                }
            );
        }).then((res)=>{
            const contactsItem:ContactsItem = DMContactsItems[nowDMContactsIndex.value];
            RTCServiceInstance.inviteJoinRoom(
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
        rtcDialogVisible.value = false;
    }, 1000);

    const rtcDialogVisible:Ref<boolean> = ref<boolean>(false);
    const deviceControl:Reactive<Devices> = reactive<Devices>({
        video: [{ name: '摄像头', active: undefined, type: 'webcam' }, { name: '投屏', active: undefined, type: 'screen' }],
        audio: [{ name: '麦克风', active: undefined }],
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

        :deep(.el-dialog){
            font-weight: bold;
            header{
                padding-left: 32px;
                padding-bottom: 6px;
                text-align: center;
            }

            .dialog-body{
                >div{
                    @include fixedHeight(100px);
                    display: flex;
                    align-items: center;
                    margin-top: 10px;

                    >div{
                        @include fullHeight;
                        display: flex;
                        align-items: center;
                        &.leftBox{
                            margin-right: 20px;
                        }

                        &.rightBox{
                            flex-grow: 1;
                        }

                        >div{
                            display: flex;
                            flex-direction: column;
                            align-items: center;
                            font-size: 12px;
                            color: black;
                            .icon{
                                @include fixedCircle(80px);
                                @include flexCenter;
                                background-color: white;
                                transition: all 0.3s ease;
                                position: relative;

                                d{
                                    position: absolute;
                                    @include fixedWidth(3px);
                                    @include fixedHeight(0%);
                                    transform: rotate(45deg);
                                    background-color: white;
                                    transition: height 0.6s ease;
                                    z-index: 2;
                                }

                                svg{
                                    position: relative;
                                    @include fixedSquare(60px);
                                    cursor: pointer;
                                    z-index: 1;
                                }
                            }

                            &[active="undefined"]{
                                .icon{
                                    svg{
                                        path{
                                            fill: #1296db;
                                            transition: all 0.3s ease;
                                        }
                                    }

                                    &:hover{
                                        background-color: #1296db;
                                        svg{
                                            path{
                                                fill: white;
                                            }
                                        }
                                    }
                                }  
                            }

                            &[active="true"]{
                                .icon{
                                    background-color: #1296db;
                                    svg{
                                        path{
                                            fill: white;
                                        }
                                    }
                                }
                            }

                            &[active="false"]{
                                .icon{
                                    background-color: red;
                                    div {
                                        @include fixedHeight(95%);
                                    }
                                    svg {
                                        path{
                                            fill: white;
                                        }
                                    }
                                }
                            }

                            &:not(:first-of-type){
                                margin-left: 20px;
                            }
                        }
                    }
                }
            }
        }
    }
</style>