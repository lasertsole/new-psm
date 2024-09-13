<template>
    <div class="userState">
        <!-- 登录注册按钮 -->
        <div v-if="!isOnline" class="loginOrRegister">
            <NuxtLink to="/loginOrRegister/login" class="login">登录</NuxtLink>
            <NuxtLink to="/loginOrRegister/register" class="register">注册</NuxtLink>
        </div>

        <ul class="userTool" v-else>
            <!-- <li class="profile" @mouseenter="showDetail" @mouseleave="hideDetail">
                <img :class="{userProfile:true, show:showUserDetail}" :src="profile" @click="onClick">
                <transition 
                    :css="false"
                    @enter="onEnter"
                    @leave="onLeave"
                >
                    <div v-show="showUserDetail" :class="{userDetail:true}">
                        <div class="name" @click="onClick">{{userName}}</div>
                        <ul class="numInfo">
                            <li>
                                <div class="top">57</div>
                                <div class="bottom">关注</div>
                            </li>
                            <li>
                                <div class="top">3</div>
                                <div class="bottom">粉丝</div>
                            </li>
                        </ul>
                        <ul class="option">
                            <li><router-link to="/accountModify"><img src="icons/profile.png"><span>账户设置</span><img src="icons/arrow.svg" alt=""></router-link></li>
                            <li><router-link to="/accountModify"><img src="icons/planning.png"><span>我的企划</span><img src="icons/arrow.svg" alt=""></router-link></li>
                            <li><router-link to="/accountModify"><img src="icons/Vector.png"><span>我的橱窗</span><img src="icons/arrow.svg" alt=""></router-link></li>
                            <hr>
                            <li @click="logout">
                                <div><img src="icons/longArrow.svg"><span>退出登录</span></div>
                            </li>
                        </ul>
                    </div>
                </transition>
            </li> -->
            <li>动态</li>
            <li>收藏</li>
            <li>历史</li>
            <li>创作中心</li>
            <li>投稿</li>
        </ul>

        <!-- 用户头像以及菜单 -->
        <div class="drawBar" @click="drawer = true"></div>

        <el-drawer
            v-model="drawer"
            :direction="direction"
            :modal="true"
            size="auto"
        >
            <ul>
                <template v-for="(item, index) in routerList" :key="index">
                    <li 
                        :class="{login:item.path=='/login',register:item.path=='/register'}"
                        v-if="item.tarbar==true||!isOnline"
                    >
                        {{item.name}}
                    </li>
                </template>
            </ul>
        </el-drawer>
    </div>
</template>

<script lang="ts" setup>
    import gsap from "gsap";
    import type { DrawerProps } from 'element-plus';
    import type { Router } from '@/types/router';

    const isOnline = ref<boolean>(false);
    const { $on } = useNuxtApp();

    $on("online", () => {
        isOnline.value = true;
    });

    $on("offline", () => {
        isOnline.value = false;
    });

    const drawer = ref(false);
    const direction = ref<DrawerProps['direction']>('ttb');

    const routerList:Router[] = getRouterList();

    /* 用户头像动画锁*/
    let isMouseInBox:boolean = false;
    let animateLock:boolean = false;
    // function showDetail():void{
    //     isMouseInBox=true;
    //     if(animateLock){return};//判断动画是否锁上
    //     showUserDetail.value=true;
    //     animate?.kill();
    // }
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    @mixin button{
        width: 80px;
        height: 36px;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin: 0px 10px;
        border-radius: 4px;
        color: white;
        cursor: pointer;
    }
    
    .userState{
        display: flex;
        align-items: center;
        .loginOrRegister{
            display: flex;
            flex-direction: row;
            .login{
                display: block;
                background-color: #fb7299;
                @include button;
            }
            .register{
                display: block;
                background-color: #00a8e9;
                @include button;
            }
        }

        .userTool{
            display: flex;
            flex-direction: row;
            font-size: 13px;
            align-items: center;
            padding: 10px;

            li{
                padding: 5px;
                border-radius: 5px;

                &.profile{
                    cursor: pointer;
                    background-size: 100%;
                    margin-right: 20px;
                    padding: 0px;
                    position: relative;
                    $profileSize: 35px;
                }
            }
        }

        .drawBar{
            background-image: url("/icons/menu.svg");
            background-size: 80%;
            background-position: center;
            background-repeat: no-repeat;
            @include fixedCircle(35px);
            display: none;
            transition: all 0.3s;
            &:hover{
                background-color: #d3d3d3;
            }
        }
        :deep(.el-drawer){
            ul{
                li{
                    background-color: #ecf5ff;
                    @include fixedRoundedRectangle(100%, 50px, 4px);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    color: #409eff;
                    margin: 10px;
                    transition: all 0.3s;
                    &:hover{
                        cursor: pointer;
                        color: #ecf5ff;
                        background-color: #409eff;
                    }
                    
                    &.login{
                        background-color: #fb7299;
                        color: #ecf5ff;
                    }
                    &.register{
                        background-color: #00a8e9;
                        color: #ecf5ff;
                    }
                }
            }
        }

        $showDraw: none;
        @media screen and (max-width: 600px) {
            .loginOrRegister{
                display: none;
            }
            .drawBar{
                display: block;
            }
        }
    }
</style>