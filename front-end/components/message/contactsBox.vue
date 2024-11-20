<template>
    <div :class="{contactsBox:true, isSeleted}"
        @click="clickEvent"
    >
        <div class="leftBox">
            <CommonAvatar :src="avatar"></CommonAvatar>
        </div>
        
        <div class="rightBox">
            <div class="top">
                <span class="name">{{ name }}</span>
                <span class="time">{{ formatToLocalTime(lastTime) }}</span>
            </div>
            <div class="bottom">{{ lastMessage }}</div>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        tgtUserId: {type:String, required: true},
        name: {type:String, required: true},
        avatar: {type:String, required: true},
        lastMessage: {type:String, required: true},
        lastTime: {type:String, required: false},
        unread: {type:Number, required: true},
        isMuted: {type:Boolean, required: true},
        isGroup: {type:Boolean, required: true},
        isSeleted: {type:Boolean, required:true},
        index: {type:Number, required: true}
    });
    
    function clickEvent():void {
        nowDMContactsIndex.value = props.index;
    };
    
    // 计算属性，用于将 UTC 时间转换为本地时间
    const formatToLocalTime = (utcTime: string | undefined) => {
        if (!utcTime) return '';

        // 检查是否为数字，可能是时间戳
        if (!isNaN(Number(utcTime))) {
            const timestamp = Number(utcTime);
            const date = new Date(timestamp);
            if (isNaN(date.getTime())) {
            console.error('Invalid date:', utcTime);
            return ''; // 或者返回其他默认值
            }
            return getFormattedDate(date);
        }

        // 将自定义格式转换为标准 ISO 8601 格式
        const isoTime = utcTime.replace(' ', 'T') + 'Z';
        const date = new Date(isoTime);
        if (isNaN(date.getTime())) {
            console.error('Invalid date:', utcTime);
            return ''; // 或者返回其他默认值
        }
        
        return getFormattedDate(date);
    };

    // 自定义日期格式化函数
    const getFormattedDate = (date: Date) => {
        const now = new Date();
        const diff = now.getTime() - date.getTime();
        const oneDay = 24 * 60 * 60 * 1000; // 一天的毫秒数
        const oneYear = 365 * oneDay; // 一年的毫秒数

        const options: Intl.DateTimeFormatOptions = {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: 'numeric',
            minute: '2-digit',
            second: '2-digit',
            hour12: false,
        };

        const formattedDate = date.toLocaleString('en-US', options);
        const [timePart, amPm] = formattedDate.split(', ');
        const [monthDayYear, time] = timePart.split(' ');
        const [month, day, year] = monthDayYear.split('/');

        if (diff >= oneYear) {
            return `${year}.${month}.${day}`;
        } else if (diff >= oneDay) {
            return `${month}.${day}`;
        } else {
            return `${amPm}`;
        };
    };

    // 示例数据
    const lastTime = props.lastTime; // 假设 lastTime 是 UTC 时间字符串
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