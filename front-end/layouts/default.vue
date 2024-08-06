<template>
    <div class="layout">
        <header>
            <div class="left">
                <HeaderLogo></HeaderLogo>
                <div class="tabBar">
                    <HeaderTabBar :tabBarArr="routerList"></HeaderTabBar>
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
    import type { Router } from '@/types/router';

    const routerList:Router[] = getRouterList().filter((item) => {
        return item.tarbar == true;
    });
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
                .left{
                }
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
        }
    }
</style>