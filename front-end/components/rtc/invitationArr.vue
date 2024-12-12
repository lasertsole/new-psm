<template>
    <div>
        <div  v-if="RTCServiceInstance&&RTCServiceInstance.inviteJoinArr.length>0" class="absolute top-1/2 right-0 transform -translate-y-1/2 z-10 bg-white rounded-tl-md rounded-bl-md bg-opacity-50 cursor-pointer">
            <div class="mt-2 mb-2 ml-2 mr-4 w-14 h-14 bg-[url('/icons/phone.svg')] bg-cover bg-center"
                @click="dialogVisible = true">
                <div class="w-5 h-5 leading-5 rounded-full bg-red-500 top-2 right-4 font-bold text-white flex items-center justify-center" style="position: absolute;">
                    {{RTCServiceInstance.inviteJoinArr.length}}
                </div>
            </div>
        </div>

        <el-dialog
            class="dialog"
            v-model="dialogVisible"
            align-center
            title="请选择要加入的房间"
            width="500"
            append-to-body
        >
            <div class="dialog-body">
                <template v-for="(item, index) in RTCServiceInstance.inviteJoinArr">
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
                                    @click="RTCServiceInstance.agreeJoinRoom(item)"
                                >同意</div>
                                <div class="button bg-red-500"
                                    @click="RTCServiceInstance.rejectJoinRoom(item)"
                                >拒绝</div>
                            </div>
                        </div>
                    </div>
                </template>  

            </div>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup>
    const dialogVisible:Ref<boolean> = ref<boolean>(false);
    let RTCServiceInstance: Ref<RTCService | undefined> = ref<RTCService | undefined>();

    on("online", ()=>{
        RTCServiceInstance.value = RTCService.getInstance();
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .button{
        @apply cursor-pointer text-white px-2 py-1 flex items-center justify-center font-bold h-8;
    };
</style>