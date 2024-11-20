<template>
    <div :class="{messageBox:true, isSelf:srcUserId==userInfo.id}">
        <div class="top">
            
        </div>
        <div class="bottom">
            <div class="avatar">
                <CommonAvatar :src="srcUserId==userInfo.id?userInfo.avatar:avatar"></CommonAvatar>
            </div>
            
            <div class="gap"></div>
            
            <div class="text"><slot name="text"></slot></div>

            <div class="gap"></div>
            
            <div class="status" v-if="status!='sent'">
                <div class="pending" v-if="status=='pending'"></div>
                <div class="error" v-else-if="status=='error'" ></div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        avatar: {type:String, required: true},
        name: {type:String, required: true},
        type: {type:String, required: true},
        srcUserId: {type:String, required: true},
        tgtUserId: {type:String, required: true},
        timestamp: {type:String, required: false},
        isDeleted: {type:Boolean, required: true},
        status: {type:String, required: false, default: "sent"}
    });
</script>

<style scoped lang="scss">
    @use "sass:math";
    @import "@/common.scss";

    .messageBox{
        &.isSelf{
            .bottom{
                flex-direction: row-reverse;
                
                .text{
                    background-color: #80b9f2;
                    border-radius: 16px 0 16px 16px;
                }
            }
        }
        
        .bottom{
            @include fullWidth;
            display: flex;
            flex-direction: row;
            align-items: start;
            padding: 0px 16px 16px 16px;
            
            $avatarSize: 30px;
            $gapSize: 8px;
            .avatar{
                @include fixedSquare($avatarSize);
            }

            .gap{
                @include fixedWidth($gapSize);
            }
            
            .text{
                max-width: calc(100% - 2 * ($avatarSize + $gapSize));
                padding: 8px 16px;
                display: inline-block;
                font-size: 14px;
                word-wrap: break-word;
                word-break: break-word;
                border-radius: 0 16px 16px 16px;
                overflow: hidden;
                background: #fff;
            }

            .status{
                @include fixedSquare(16px);
                align-self: flex-end;

                >div{
                    @include fullInParent;
                    background-size: 100%;
                    background-repeat: no-repeat;
                    background-position: center;
                }
                
                .pending{
                    @keyframes spin {
                        0% { transform: rotate(0deg); }
                        100% { transform: rotate(360deg); }
                    }
                    animation: spin 2s linear infinite;
                    background-image: url("/icons/msPending.svg");
                }

                .error{
                    background-image: url("/icons/msError.svg");
                }
            }
        }
    }
</style>