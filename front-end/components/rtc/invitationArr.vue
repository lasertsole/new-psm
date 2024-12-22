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
                                    <svg t="1733564168472" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3711" v-if="item.type=='microphone'">
                                        <path d="M512 128c35.2 0 64 28.8 64 64v320c0 35.2-28.8 64-64 64s-64-28.8-64-64V192c0-35.2 28.8-64 64-64m0-64c-70.4 0-128 57.6-128 128v320c0 70.4 57.6 128 128 128s128-57.6 128-128V192c0-70.4-57.6-128-128-128z m320 448h-64c0 140.8-115.2 256-256 256S256 652.8 256 512h-64c0 165.6 126.4 302.4 288 318.4V960h64v-129.6c161.6-16 288-152.8 288-318.4z" p-id="3712"></path>
                                    </svg>
                                    <svg t="1734755245136" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2424" v-else-if="item.type=='speaker'">
                                        <path d="M830.450526 853.759999q-11.722105 8.791579-27.351579 8.791579-19.536842 0-33.701053-14.164211t-14.164211-33.701053q0-21.490526 16.606316-36.143158 0.976842-0.976842 1.953684-1.465263t1.953684-1.465263l0.976842-0.976842q27.351579-18.56 50.795789-43.957895t41.027368-55.191579 27.351579-63.494737 9.768421-69.84421q0-73.263158-37.12-133.827368t-92.8-99.637895q-20.513684-14.652632-20.513684-39.073684 0-19.536842 14.164211-33.701053t33.701053-14.164211q16.606316 0 29.305263 10.745263 36.143158 25.397895 67.402105 59.098947t53.726316 73.263158 35.166316 84.496842 12.698947 92.8q0 48.842105-12.698947 93.776842t-35.654737 84.985263-54.214737 73.751579-68.378947 59.098947zM775.747368 415.157894q20.513684 28.328421 32.72421 57.145263t12.210526 69.84421q0 39.073684-12.698947 70.332632t-32.235789 56.656842q-7.814737 10.745263-16.606316 19.048421t-22.467368 8.303158q-17.583158 0-29.793684-12.698947t-12.210526-30.282105q0-7.814737 2.930526-15.629474l-0.976842 0q4.884211-10.745263 11.722105-20.513684t13.187368-20.025263 10.745263-23.444211 4.395789-31.747368q0-17.583158-4.395789-30.770526t-10.745263-23.932632-13.187368-20.513684-10.745263-20.513684q-2.930526-6.837895-2.930526-15.629474 0-17.583158 12.210526-30.282105t29.793684-12.698947q13.675789 0 22.467368 8.303158t16.606316 19.048421zM460.227368 995.402104q-49.818947-44.934737-105.498947-93.776842t-103.545263-89.869474q-55.68-46.888421-111.36-92.8-10.745263 0.976842-21.490526 0.976842-8.791579 0.976842-18.56 0.976842l-16.606316 0q-26.374737 0-42.981053-16.117895t-16.606316-38.585263l0-246.16421 0.976842 0-0.976842-0.976842q0-27.351579 17.094737-44.934737t42.492632-17.583158l55.68 0q89.869474-76.193684 163.132631-136.757895 31.258947-26.374737 61.541053-51.28421t54.703158-45.423158 41.027368-34.189474 20.513684-16.606316q29.305263-21.490526 47.376842-19.536842t28.328421 17.583158 14.164211 38.096842 3.907368 41.027368l0 788.311578 0 2.930526q0 18.56-6.837895 39.562105t-21.002105 33.212632-35.654737 10.256842-49.818947-28.328421z" p-id="2425" fill="#1296db"></path>
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
        video: [{ name: '摄像头', active: undefined, type: 'webcam', bindStreams: [], seletedStreamIndex: -1 }, { name: '投屏', active: undefined, type: 'screen', bindStreams: [], seletedStreamIndex: -1 }],
        audio: [{ name: 'PC音效', active: undefined, type: 'speaker', bindStreams: [], seletedStreamIndex: -1 }, { name: '麦克风', active: undefined, type: 'microphone', bindStreams: [], seletedStreamIndex: -1 }],
    });

    const dplayerRef:Ref<InstanceType<typeof dplayer> | undefined> = ref<InstanceType<typeof dplayer> | undefined>();

    // 打开邀请窗口
    function trackSwitchComplete():void{
        invitationArrVisible.value = false;
        trackSwitchVisible.value = false;
        videoVisible.value = true;
    };

    on("online", async():Promise<void>=>{
        await nextTick();// // 确保组件已挂载
        RTCServiceInstance = await RTCService.getInstance();
        RTCServiceInstance.initVideoDom(dplayerRef.value!.dpDomRef!, dplayerRef.value!.minDpDomRef!);// 初始化videoDom
        RTCServiceInstance.onSwapCandidate((remoteSDP: RTCSwap):void=>{// 添加链接建立事件的回调
            trackSwitchVisible.value = true;
        });

        // 在建立连接后,获取本地媒体流
        RTCServiceInstance.onTrackBulid(async (event)=>{
            const { userMediaStream, displayMediaStream } = await RTCServiceInstance!.getLocalStream();
            deviceControl.video[0].bindStreams= displayMediaStream.getVideoTracks(); 
            deviceControl.video[1].bindStreams= userMediaStream.getVideoTracks();
            deviceControl.audio[0].bindStreams= displayMediaStream.getAudioTracks();
            deviceControl.audio[1].bindStreams= userMediaStream.getAudioTracks();
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

    // 切换媒体流开关
    const toggleStreamSwitch = debounce(()=>{
        
    }, 500);

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

                            div{
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