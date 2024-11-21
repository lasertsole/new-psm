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
        avatar: {type:String, required: true},
        unread: {type:Number, required: true},
        isMuted: {type:Boolean, required: true},
        isGroup: {type:Boolean, required: true},
        isSeleted: {type:Boolean, required:true},
        index: {type:Number, required: true}
    });
    
    function clickEvent():void {
        nowDMContactsIndex.value = props.index;
    };
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