<template>
    <div :class="{messageBox:true, isSelf:srcUserId==userInfo.id}">
        <div class="avatar">
            <CommonAvatar :src="srcUserId==userInfo.id?userInfo.avatar:avatar"></CommonAvatar>
        </div>
        
        <div class="gap"></div>
        
        <div class="content">
            <div class="text"><slot name="text"></slot></div>
        </div>

        <div class="gap"></div>
        
        <div class="status" v-if="status!='sent'">
            <div class="pending" v-if="status=='pending'"></div>
            <div class="error" v-else-if="status=='error'" ></div>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        avatar: {type:String, required: false, default: process.env.Default_User_Avatar},
        name: {type:String, required: false, default: ""},
        type: {type:String, required: false, default: 'text'},
        srcUserId: {
            type:String,
            required: true,
            validator: function (value:string):boolean {
                try {
                    return parseInt(value)>=0;
                } catch (error) {
                    return false;
                };
            }
        },
        tgtUserId: {
            type:String,
            required: true,
            validator: function (value:string):boolean {
                try {
                    return parseInt(value)>=0;
                } catch (error) {
                    return false;
                };
            }
        },
        timestamp: {type:String, required: false},
        isDeleted: {type:Boolean, required: false, default: false},
        status: {type:String, required: false, default: "sent"}
    });
</script>

<style scoped lang="scss">
    @use "sass:math";
    @use "@/common.scss" as common;

    .messageBox{
        @include common.fullWidth;
        display: flex;
        flex-direction: row;
        align-items: start;
        padding: 0px 16px 16px 16px;
        
        $avatarSize: 30px;
        $gapSize: 8px;

        .avatar{
            @include common.fixedSquare($avatarSize);
        }

        .gap{
            @include common.fixedWidth($gapSize);
        }
        
        .content{
            max-width: calc(100% - 2 * ($avatarSize + $gapSize));
            display: flex;
            flex-direction: column;

            .text{
                padding: 8px 16px;
                display: inline-block;
                font-size: 14px;
                word-wrap: break-word;
                word-break: break-word;
                border-radius: 0 16px 16px 16px;
                overflow: hidden;
                background: #fff;
            }
        }

        .status{
            @include common.fixedSquare(16px);
            align-self: flex-end;

            >div{
                @include common.fullInParent;
                background-size: 100%;
                background-repeat: no-repeat;
                background-position: center;
            }
            
            .pending{
                animation: spin 2s linear infinite;
                background-image: url("/icons/msPending.svg");
                @keyframes spin {
                    0% { transform: rotate(0deg); }
                    100% { transform: rotate(360deg); }
                }
            }

            .error{
                background-image: url("/icons/msError.svg");
            }
        }

        &.isSelf{
            flex-direction: row-reverse;
            
            .text{
                background-color: #80b9f2;
                border-radius: 16px 0 16px 16px;
            }
        }
    }
</style>