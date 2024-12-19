<template>
    <div>
        <div class="absolute top-1/2 right-0 transform -translate-y-1/2 z-10 bg-white rounded-tl-md rounded-bl-md bg-opacity-50 cursor-pointer"
            v-if="userInfo.isLogin&&RTCServiceInstance&&RTCServiceInstance!.inviteJoinArr.length>0"
        >
            <div class="mt-2 mb-2 ml-2 mr-4 w-14 h-14 bg-[url('/icons/phone.svg')] bg-cover bg-center"
                @click="invitationArrVisible = true">
                <div class="w-5 h-5 leading-5 rounded-full bg-red-500 top-2 right-4 font-bold text-white flex items-center justify-center" style="position: absolute;">
                    {{RTCServiceInstance!.inviteJoinArr.length}}
                </div>
            </div>
        </div>

        <el-dialog
            class="dialog"
            v-model="invitationArrVisible"
            align-center
            title="请选择要加入的房间"
            width="500"
            append-to-body
        >
            <div class="dialog-invitation">
                <template v-for="(item, index) in RTCServiceInstance!.inviteJoinArr" :key="index">
                <div class="flex flex-col bg-white hover:bg-blue-100 transition-colors duration-300 ease-in-out rounded p-3">
                    <div class="font-bold">来自{{item.srcUserName}}的房间邀请</div>
                        <div class="flex justify-between">
                            <div class="flex justify-between">
                                <div>
                                    <div>房间号: </div>
                                    <div>房间类型: </div>
                                </div>
                                <div class="ml-2">
                                    <div>{{item.roomName}}</div>
                                    <div>{{item.roomType}}</div>
                                </div>
                            </div>

                            <div class="flex justify-center">
                                <div class="button bg-blue-500 mr-3"
                                    @click="agreeJoinRoom(item)"
                                >同意</div>
                                <div class="button bg-red-500"
                                    @click="rejectJoinRoom(item)"
                                >拒绝</div>
                            </div>
                        </div>
                    </div>
                </template>  

            </div>
        </el-dialog>

        <el-dialog
            v-model="trackSwitchVisible"
            align-center
            title="请选择音频输入"
            width="500"
        >
            <div class="dialog-rtcOptions">
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
                    <el-button type="primary" @click="trackSwitchComplete">确认</el-button>
                </div>
            </template>
        </el-dialog>
        
        <commonDplayer ref="dplayerRef"
            :show="videoVisible"
            :hideController="true"
            :PIPController="true"
            :hasMinVideo="true"
        >
        </commonDplayer>
    </div>
</template>

<script lang="ts" setup>
    import type { Reactive } from 'vue';
    import type { Devices, RTCSwap } from "@/types/rtc";
    import type { RoomInvitation } from "@/types/common";
    import dplayer from "@/components/common/dplayer.vue";

    // 模态窗口可见性
    const invitationArrVisible:Ref<boolean> = ref<boolean>(false);

    // RTC窗口可见性
    const trackSwitchVisible:Ref<boolean> = ref<boolean>(false);

    // 视频窗口可见性
    const videoVisible:Ref<boolean> = ref<boolean>(false);

    let RTCServiceInstance: RTCService | null = null;//RTC服务实例

    const deviceControl:Reactive<Devices> = reactive<Devices>({
        video: [{ name: '摄像头', active: undefined, type: 'webcam' }, { name: '投屏', active: undefined, type: 'screen' }],
        audio: [{ name: '麦克风', active: undefined }],
    });

    const dplayerRef:Ref<InstanceType<typeof dplayer> | null> = ref<InstanceType<typeof dplayer> | null>(null);

    // 打开邀请窗口
    function trackSwitchComplete():void{
        invitationArrVisible.value = false;
        trackSwitchVisible.value = false;
        videoVisible.value = true;
    };

    let thisUserMediaStream: MediaStream | null = null;// 本地音视频流
    let thisDisplayMediaStream: MediaStream | null = null;// 当前投屏媒体流
    on("online", async()=>{
        RTCServiceInstance = RTCService.getInstance();
        RTCServiceInstance.initVideoDom(dplayerRef.value!.dpDomRef!, dplayerRef.value!.minDpDomRef!);// 初始化videoDom
        RTCServiceInstance.onSwapCandidate((remoteSDP: RTCSwap):void=>{// 添加链接建立事件的回调
            trackSwitchVisible.value = true;
        });

        // 在建立连接后,获取本地媒体流
        RTCServiceInstance.onTrackBulid(async (event)=>{
            const { userMediaStream, displayMediaStream } = await RTCServiceInstance!.getLocalStream();
            thisUserMediaStream=userMediaStream;
            thisDisplayMediaStream=displayMediaStream;
        });
    });

    on("offline", ()=>{
        // 销毁实例
        RTCService.destroyInstance();
        RTCServiceInstance = null;
    });

    // 邀请加入房间
    const agreeJoinRoom = debounce(async (item: RoomInvitation):Promise<void>=> {
        await new Promise((resolve)=>{
            RTCServiceInstance!.agreeJoinRoom(
                item,
                ()=>{
                    invitationArrVisible.value = false;
                    resolve(true);
                    return;
                },
                ()=>{
                    throw new Error("crash when invite agreeJoinRoom");
                }
            );
        }).catch((e)=>{
            ElMessage.warning(e);
        });
    }, 1000);

    // 拒绝加入房间
    const rejectJoinRoom = debounce(async (item: RoomInvitation):Promise<void>=>{
        await RTCServiceInstance!.rejectJoinRoom(item);
        invitationArrVisible.value = false;
    }, 1000);
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .button{
        @apply cursor-pointer text-white px-2 py-1 flex items-center justify-center font-bold h-8;
    };

    :deep(.el-dialog) {
        font-weight: bold;
        header{
            padding-left: 32px;
            padding-bottom: 6px;
            text-align: center;
        }

        .dialog-rtcOptions {
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
</style>