<template>
    <div class="layout">
        <header>
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
        <main>
            <slot></slot>
        </main>
    </div>
</template>

<script setup lang="ts">
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .layout{
        @include fullViewWindow;
        display: flex;
        flex-direction: column;
        overflow: hidden;
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
                    @include fixedSquare(25px);
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
            flex-grow: 1;
            overflow: auto;

            /* 滚动条整体部分 */
            &::-webkit-scrollbar {
            width: 10px; /* 滚动条的宽度 */
            height: 10px; /* 水平滚动条的高度 */
            }

            /* 滚动条的滑块部分 */
            &::-webkit-scrollbar-thumb {
            background-color: darkgrey; /* 滑块的颜色 */
            border-radius: 10px; /* 滑块的圆角 */
            }

            /* 滚动条的轨道部分 */
            &::-webkit-scrollbar-track {
            background-color: lightgrey; /* 轨道的颜色 */
            border-radius: 10px; /* 轨道的圆角 */
            }
        }
    }
</style>