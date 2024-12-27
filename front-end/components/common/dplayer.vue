<template>
    <div class="rtc absolute"
        :class="{minilize}"
        v-show="show" ref="rtcBoxDom"
        @mousedown="PIPDrag"
        @contextmenu.prevent="contextmenuEvent"
    >
        <div v-show="PIPController" class="overlay w-full h-full" @dblclick="fullScreen"></div>
        <div ref="dplayerDom"></div>
    </div>

    <CommonContextmenu
        v-click-outside="contextmenuOutsideEvent"
        :UlClass="`bg-gray-800 shadow-md w-[150px]`"
        :liClass="`text-white text-center w-full transition-all duration-300 hover:text-cyan-500`"
        :isVisible="contextMenuVisible" 
        :top="contextMenuTop" 
        :left="contextMenuLeft" 
        :contextMenuOptions="contextMenuOptions"
    >
    </CommonContextmenu>

    <template ref="minVideoTemplate">
        <div v-if="hasMinVideo" class="minVideo bg-white absolute bottom-0 right-0" :class="{changeBigger: isFullScreen}">
            <video ref="minDpDomRef"></video>
        </div>
    </template>
</template>

<script setup lang="ts">
    import type { Reactive} from 'vue';
    import type DPlayer from 'dplayer';
    import type { ContextMenuOptions } from "@/types/common";

    const props = defineProps({
        show: {type:Boolean, required: false, default: false},
        hideController: {type:Boolean, required: false, default: false},
        PIPController: {type:Boolean, required: false, default: false},
        hasMinVideo: {type:Boolean, required: false, default: false},
    });

    const isFullScreen: Ref<boolean> = ref<boolean>(false);// 是否全屏
    const minilize: Ref<boolean> = ref<boolean>(false);// 是否最小化

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
        initWindowSize();
        oldBiasLeft= (windowWidth- boxWidth)/2;
        oldBiasTop= (windowHeight - boxHeight)/2;
        rtcBoxDom.value!.animate({transform: `translate(${oldBiasLeft}px, ${oldBiasTop}px)`},{
            duration: 0,
            fill: 'forwards',
        });

        dp.on('fullscreen', ()=>{
            isFullScreen.value=true;
        });

        dp.on('fullscreen_cancel', ()=>{
            isFullScreen.value=false;
        });
    });

    const fullScreen = debounce(()=>{
        dp.fullScreen.request("browser");
        isFullScreen.value = true;
        contextMenuVisible.value = false;// 隐藏右键菜单
    }, 500);

    /**以下是画中画拖拽部分**/
    const rtcBoxDom:Ref<HTMLElement | undefined> = ref<HTMLElement | undefined>();
    let boxWidth:number;// 画中画宽度
    let boxHeight:number;// 画中画高度
    const boxLeft = ref<string>("");//画中画距离盒子右边距离
    const boxTop = ref<string>("");//画中画距离盒子底端距离
    
    let biasLeft:number; // 盒子到父容器的左边缘的距离
    let biasTop:number; // 盒子到父容器的上边缘的距离
    let oldBiasLeft:number;// 上一次盒子到父容器的左边缘的距离
    let oldBiasTop:number;// 上一次盒子到父容器的上边缘的距离
    let al:number; //鼠标到盒子左边缘的距离
    let at:number; //鼠标到盒子上边缘的距离
    let windowWidth:number;// 窗口宽度
    let windowHeight:number;// 窗口高度

    // 计算画中画位置
    function computedPosition():void {
        rtcBoxDom.value!.getAnimations().forEach(animation => {
            animation.cancel();
        });

        if(biasLeft<0) biasLeft=0; //避免超出窗口左边缘
        if(biasLeft+boxWidth>windowWidth) biasLeft = windowWidth - boxWidth;

        if(biasTop<0) biasTop=0;// 避免超出窗口上边缘
        if(biasTop+boxHeight>windowHeight) biasTop = windowHeight - boxHeight;

        rtcBoxDom.value!.animate([
            {transform: `translate(${oldBiasLeft}px, ${oldBiasTop}px)`},
            {transform: `translate(${biasLeft}px, ${biasTop}px)`}
        ], {
            duration: 0,
            fill: 'forwards',
        });

        // 刷新旧值
        oldBiasLeft = biasLeft;
        oldBiasTop = biasTop;
    };

    function initWindowSize():void {
        windowWidth = (window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth);
        windowHeight = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight);
    };

    function PIPDrag(event:any):void{//拖拽视频盒子
        if(props.PIPController) {
            event = event || window.event;

            al = event.clientX - oldBiasLeft; //求出鼠标到盒子左边缘的距离
            at = event.clientY - oldBiasTop; //求出鼠标到盒子上边缘的距离
            initWindowSize();

            document.onmousemove = throttle((event):void=> {
                event = event || window.event;
                biasLeft = event.clientX - al;
                biasTop = event.clientY - at ;

                computedPosition();
            }, 40);

            document.onmouseup = function() {
                this.onmousemove = null; 
                this.onmouseup = null;
            };
        }
    }
    /**以上是画中画拖拽部分**/

    /**以下是自定义右键菜单部分**/
    const contextMenuVisible:Ref<boolean> = ref<boolean>(false);
    const contextMenuTop:Ref<number> = ref<number>(0);
    const contextMenuLeft:Ref<number> = ref<number>(0);

    async function minilizeChange():Promise<void> {// 最小化按钮触发
        minilize.value = !minilize.value;
        contextMenuVisible.value = false;
        await nextTick();
        boxWidth = rtcBoxDom.value!.getBoundingClientRect().width;
        boxHeight = rtcBoxDom.value!.getBoundingClientRect().height;
        computedPosition();
    };

    const computedMinilizeText: Ref<string> = computed(()=>{// 最小化按钮文字
        return minilize.value ? "窗口化" : "最小化";
    });
    const contextMenuOptions:Reactive<ContextMenuOptions[]> = reactive<ContextMenuOptions[]>(
        [
            {text: "全屏", callback: fullScreen},
            {text: computedMinilizeText, callback: minilizeChange}
        ]
    );
    const contextmenuEvent = debounce((event:MouseEvent)=>{
        contextMenuVisible.value = true;
        contextMenuTop.value = event.clientY;
        contextMenuLeft.value = event.clientX;
    }, 100);

    const contextmenuOutsideEvent = debounce(()=>{
        contextMenuVisible.value = false;
    }, 100);
    /**以上是自定义右键菜单部分**/

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
        left: 0px;
        top: 0px;

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

        &.minilize{
            @include fixedCircle(40px);
            .overlay{
                background-color: rgb(31 41 55);
                background-image: url(/icons/rtc.svg);
                background-position: center;
                background-repeat: no-repeat;
                background-size: 100%;
            }
        };
    }
</style>