<template>
    <div :class="{contactsBox:true, isSeleted}"
        @click="clickEvent"
    >
        <div class="leftBox">
            <CommonAvatar :src="avatar"></CommonAvatar>
        </div>
        
        <div class="rightBox">
            <div class="top">
                <span class="name"><slot name="name"></slot></span>
                <span class="time"><slot name="lastTime"></slot></span>
            </div>
            <div class="bottom">
                <slot name="lastMessage"></slot>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        tgtUserId: {type:String, required: true},
        avatar: {type:String, required: false, default: process.env.Default_User_Avatar},
        unread: {type:Number, required: false, default: 0},
        isMuted: {type:Boolean, required: false, default: false},
        isGroup: {type:Boolean, required: false, default: false},
        isSeleted: {type:Boolean, required:true},
        index: {type:Number, required: true},
        callBack: {type:Function, required: true},
    });
    
    async function clickEvent():void {
        await DMServiceInstance.changeIndex(props.index);
        props.callBack();
    };

    let DMServiceInstance: DMService;//DM服务实例
    onMounted(()=>{
        if(!userInfo.isLogin) return;
        DMServiceInstance=DMService.getInstance();
    });
</script>

<style scoped lang="scss">
    @use "sass:math";
    @import "@/common.scss";

    .contactsBox{
        @include fullWidth;
        @include fixedHeight(80px);
        
        padding: 20px 24px;
        display: flex;
        align-items: center;
        
        .leftBox{
            @include fixedSquare(40px);
            @include flexCenter;
            margin-right: 8px;
        }

        .rightBox{
            @include fullHeight;
            flex-grow: 1;
            display: flex;
            flex-direction: column;

            .top{
                @include fullWidth();
                @include fixedHeight(50%);
                display: flex;
                justify-content: space-between;
                align-items:flex-end;
                
                .name{
                    @include wordEllipsis;
                }

                .time{
                    font-size: 12px;
                }
            }
            
            .bottom{
                @include fullWidth();
                @include fixedHeight(50%);
                @include wordEllipsis;
                color: #707070;
                // 添加以下样式以实现文字换行并限制显示的行数
            }
        }
        
        
        &.isSeleted{
            transition: all .3s;
            background-color: #e4e5e6;
        }
        
        &:hover{
            transition: all .3s;
            background-color: #e4e5e6;
        }
    }
</style>