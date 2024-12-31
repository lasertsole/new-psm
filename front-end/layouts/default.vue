<template>
    <div class="layout" ref="layout">
        <header ref="header">
            <div class="left">
                <HeaderLogo></HeaderLogo>
                <div class="tabBar">
                    <HeaderTabBar></HeaderTabBar>
                </div>
            </div>
            <div class="right">
                <HeaderUserState></HeaderUserState>
            </div>
        </header>
        <main class="relative">
            <slot></slot>
        </main>
    </div>
</template>

<script setup lang="ts">
    const layout: Ref<HTMLElement | undefined> = ref<HTMLElement>();
    const header: Ref<HTMLElement | undefined> = ref<HTMLElement>();
    const nowMainHeight:Ref<string | undefined> = ref('100%');

    function computedMainHeight(): void{
        if(!header.value || !layout.value) return;
        nowMainHeight.value = layout.value.clientHeight - header.value.offsetHeight + "px";
    }
    
    onMounted(() => {
        computedMainHeight();

        window.addEventListener('resize', computedMainHeight);
    })
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;

    .layout{
        @include common.fullViewWindow;
        display: flex;
        flex-direction: column;
        overflow: hidden;
        background-color: rgba(222, 222, 222, .75);
        
        header{
            transition: height .3s linear;
            position: relative;
            z-index: 2;
            background: white;
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: space-between;
            flex-shrink: 0;
            height: 80px;
            width: 100%;
            padding: 0px 30px;
            white-space: nowrap;
            box-sizing: border-box;
            border-bottom: 1px solid rgba(165, 165, 165, 0.3568627451);

            .left{
                height: 100%;
                display: flex;
                flex-direction: row;
                align-items: center;
                .tabBar{
                    display: flex;
                    align-items: center;
                }
            }

            .right{
                .menu{
                    display: none;
                    @include common.fixedSquare(25px);
                    background-image: url(icons/menu.svg);
                    background-size: 100%;
                    cursor: pointer;
                }
            }

            @media screen and (max-width: 800px) and (min-width: 600px) {
                padding: 0px 0px;
            }
            @media screen and (max-width: 600px) {
                padding: 0px 15px;
                height: 50px;
                .left{
                    .tabBar{
                        display: none;
                    }
                }
                .right{
                    .menu{
                        display: flex;
                    }
                }
            }
        }
        
        main{
            z-index: 1;
            flex-grow: 1;
            $nowMainHeight: v-bind(nowMainHeight);
            overflow: hidden;
            @include common.fixedHeight($nowMainHeight);
        }
    }
</style>