<template>
    <div class="rtc absolute"
        v-show="show" ref="rtcBoxDom"
        @mousedown="PIPDrag"
        @contextmenu.prevent="contextmenu"
    >
        <div v-show="PIPController" class="overlay w-full h-full" @dblclick="fullScreen"></div>
        <div ref="dplayerDom"></div>
    </div>

    <template ref="minVideoTemplate">
        <div v-if="hasMinVideo" class="minVideo bg-white absolute bottom-0 right-0" :class="{changeBigger: isFullScreen}">
            <video ref="minDpDomRef"></video>
        </div>
    </template>
</template>

<script setup lang="ts">
import type DPlayer from 'dplayer';

    const props = defineProps({
        show: {type:Boolean, required: false, default: true},
        hideController: {type:Boolean, required: false, default: false},
        PIPController: {type:Boolean, required: false, default: false},
        hasMinVideo: {type:Boolean, required: false, default: false},
    });

    const isFullScreen: Ref<boolean> = ref<boolean>(false);// 是否全屏  

    const isHideController = computed(()=>{
        return props.hideController ? "none" : 'block';
    });

    const dplayerDom:Ref<HTMLElement | undefined> = ref<HTMLElement | undefined>();
    let dp:DPlayer;
    const dpDomRef:Ref<HTMLVideoElement | null> = ref<HTMLVideoElement | null>(null);
    let dpParentDom:HTMLElement; // 播放器的父元素
    let minVideoTemplate:Ref<HTMLVideoElement | null> = ref<HTMLVideoElement | null>(null);
    const minDpDomRef:Ref<HTMLVideoElement | null> = ref<HTMLVideoElement | null>(null);// 右下角小video元素

    // 初始化DPlayer
    onMounted(async ()=>{
        const DPlayerModule = await import('dplayer').then();
        dp = new DPlayerModule.default({
            container: dplayerDom.value,
            video: {
                url: undefined,
                type: 'auto'
            },
            preload: 'none'
        });

        dpDomRef.value = dp.video;
        dpParentDom = dp.video.parentElement!;
        dpParentDom.appendChild(minVideoTemplate.value!.firstChild!);
        boxWidth = rtcBoxDom.value!.getBoundingClientRect().width;
        boxHeight = rtcBoxDom.value!.getBoundingClientRect().height;
        boxRight.value = `50% - ${boxWidth / 2}px`;
        boxBottom.value = `50% - ${boxHeight / 2}px`;

        dp.on('fullscreen', ()=>{
            isFullScreen.value=true;
        });

        dp.on('fullscreen_cancel', ()=>{
            isFullScreen.value=false;
        });
    });

    const contextmenu = debounce(()=>{

    }, 500);

    const fullScreen = debounce(()=>{
        dp.fullScreen.request("browser");
        isFullScreen.value = true;

        // dpParentDom.appendChild(minVideoTemplate.value!.innerHTML)
    }, 500);

    /**以下是画中画拖拽部分**/
    const rtcBoxDom:Ref<HTMLElement | undefined> = ref<HTMLElement | undefined>();
    let boxWidth:number;// 画中画宽度
    let boxHeight:number;// 画中画高度
    const boxRight = ref<string>("");//画中画距离盒子右边距离
    const boxBottom = ref<string>("");//画中画距离盒子底端距离
    
    function PIPDrag(event:any):void{//拖拽视频盒子
        if(props.PIPController) {
            event = event || window.event;
            let al:number = event.clientX - rtcBoxDom.value!.offsetLeft; //求出鼠标到盒子左边缘的距离
            let at:number = event.clientY - rtcBoxDom.value!.offsetTop; //求出鼠标到盒子上边缘的距离
            let biasLeft:number;
            let biasTop:number;
            let windowWidth = (window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth);
            let windowHeight = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight);
            
            let boxRightDight:number;
            let boxBottomDight:number;

            document.onmousemove = throttle((event):void=> {
                event = event || window.event;
                biasLeft = event.clientX - al;
                biasTop = event.clientY - at ;

                boxRightDight = windowWidth - biasLeft - parseInt(getComputedStyle(rtcBoxDom.value!).width);
                if(boxRightDight<0) boxRightDight=0; //避免超出窗口右边缘
                if(biasLeft<0) boxRightDight=windowWidth-boxWidth; //避免超出窗口左边缘

                boxBottomDight = windowHeight - biasTop - parseInt(getComputedStyle(rtcBoxDom.value!).height);
                if(boxBottomDight<0) boxBottomDight=0;// 避免超出窗口下边缘
                if(biasTop<0) boxBottomDight=windowHeight-boxHeight;// 避免超出窗口上边缘
                
                boxRight.value = boxRightDight + "px";
                boxBottom.value = boxBottomDight + "px";
            }, 40);

            document.onmouseup = function() {
                this.onmousemove = null; 
                this.onmouseup = null;
            };
        }
    }
    /**以上是画中画拖拽部分**/

    defineExpose({
        dpDomRef,
        minDpDomRef
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .rtc{
        @include fixedRetangle(250px, 150px);
        position: absolute;
        z-index: 2;
        $boxRight: v-bind(boxRight);
        $boxBottom: v-bind(boxBottom);
        right: calc($boxRight);
        bottom: calc($boxBottom);

        .overlay{
            position: absolute;
            z-index: 2;
        }

        .dplayer{
            position: relative;
            z-index: 1;
            @include fullInParent;
            :deep(.dplayer-controller) {
                $hideController: v-bind(isHideController);
                display: $hideController;
            }

            .minVideo{
                @include fixedRetangle(35%, 35%);
                &.changeBigger{
                    @include fixedRetangle(20%, 20%);
                }
            }
        }
    }
</style>