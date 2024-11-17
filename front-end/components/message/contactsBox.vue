<template>
    <div :class="{contactsBox:true, isSeleted}">
        <div class="leftBox">
            <CommonAvatar :src="avatar"></CommonAvatar>
        </div>
        
        <div class="rightBox">
            <div class="top">{{ name }}</div>
            <div class="bottom">{{ lastMessage }}</div>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        id: {type:String, required: true},
        name: {type:String, required: true},
        avatar: {type:String, required: true},
        lastMessage: {type:String, required: true},
        lastTime: {type:String, required: true},
        unread: {type:Number, required: true},
        isMuted: {type:Boolean, required: true},
        isGroup: {type:Boolean, required: true},
        isSeleted: {type:Boolean, required:true},
    });

    console.log(props.isSeleted);
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
            @include fixedHeight(40px);
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
                align-items:flex-end;
            }
            
            .bottom{
                @include fullWidth();
                @include fixedHeight(50%);
                display: flex;
                align-items:flex-start;
            }
        }
        
        
        &.isSeleted{
            background-color: #e4e5e6;
        }
    }
</style>