<template>
    <div class="userState">
        <!-- 登录注册按钮 -->
        <div v-if="!userInfo.isLogin" class="loginOrRegister">
            <NuxtLink to="/login" class="login">登录</NuxtLink>
            <NuxtLink to="/register" class="register">注册</NuxtLink>
        </div>

        <ul class="userTool" v-else>
            <li class="avatar" @mouseenter="showDetail" @mouseleave="hideDetail">
                <img :class="{userAvatar:true, show:showUserDetail}" :src="userInfo.avatar" @click="toPersionInfo">
                <transition 
                    :css="false"
                    @enter="onEnter"
                    @leave="onLeave"
                >
                    <div v-show="showUserDetail" :class="{userDetail:true}">
                        <div class="name" @click="toPersionInfo">{{userInfo.name}}</div>
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
                            <li>
                                <router-link to="/accountInfoModify">
                                    <img src="/icons/avatar.png">
                                    <span>账户设置</span>
                                    <img src="/icons/arrow.svg" alt="">
                                </router-link>
                            </li>
                            <li>
                                <router-link to="/accountInfoModify">
                                    <img src="/icons/planning.png">
                                    <span>我的企划</span>
                                    <img src="/icons/arrow.svg" alt="">
                                </router-link>
                            </li>
                            <li>
                                <router-link to="/accountInfoModify">
                                    <img src="/icons/Vector.png">
                                    <span>我的橱窗</span>
                                    <img src="/icons/arrow.svg" alt="">
                                </router-link>
                            </li>
                            <hr>
                            <li @click="logout">
                                <div>
                                    <img src="/icons/longArrow.svg">
                                    <span>退出登录</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                </transition>
            </li>
            <li>动态</li>
            <li>收藏</li>
            <li>历史</li>
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
                        v-if="(item.tarbar==true||!userInfo.isLogin)&&(item.needOffLine?!userInfo.isLogin:true)"
                        @click="drawer = false"
                    >
                        <NuxtLink active-class="selected" class="tabbarChildItem" :to="item.path">{{item.name}}</NuxtLink>
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

    const drawer = ref(false);
    const direction = ref<DrawerProps['direction']>('ttb');

    // 获取路由列表
    const routerList:Router[] = getRouterList();

    /* 用户头像动画锁*/
    let animate:any = undefined;//gsap动画容器
    let isMouseInBox:boolean = false;
    let animateLock:boolean = false;
    /*鼠标移入时展示用户细节*/
    const showUserDetail = ref<boolean>(false);
    function showDetail():void{
        isMouseInBox=true;
        if(animateLock){return};//判断动画是否锁上
        showUserDetail.value=true;
        animate?.kill();
    }

    
    /*鼠标移出时隐藏用户细节*/
    function hideDetail():void{
        isMouseInBox=false;
        if(animateLock){return};//判断动画是否锁上
        showUserDetail.value=false;
        animate?.kill();
    }

    /*详情盒子动画钩子*/
    function onEnter(el:any, done:Function):void{
        animate = gsap.to(el,{
            opacity:1,
            top: 70,
            duration: .3,//持续时间
            onStart:()=>{//开始触发函数
                animateLock=true;
            },
            onComplete:()=>{//结束触发函数
                done();
                animateLock=false;
                if(!isMouseInBox){
                    hideDetail();
                }
            }
        });
    }
    function onLeave(el:any, done:Function):void{
        animate = gsap.to(el,{
            opacity:0,
            top: 30,
            duration: .3,//持续时间
            onStart:()=>{//开始触发函数
                animateLock=true;
            },
            onComplete:()=>{//结束触发函数
                done();
                animateLock=false;
                if(isMouseInBox){
                    showDetail();
                }
            }
        });
    }

    function toPersionInfo():void{//点击触发事件
        navigateTo("/accountInfo");
    }
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

                &.avatar{
                    cursor: pointer;
                    background-size: 100%;
                    margin-right: 20px;
                    padding: 0px;
                    position: relative;
                    $profileSize: 35px;

                    .userAvatar{
                        @include fixedCircle($profileSize);
                        z-index: 2;
                        position: relative;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        transition: linear .3s;
                        background-color: white;

                        &.show{
                            transform: scale(2) translateY(75%);
                        }
                    }

                    .userDetail{
                        opacity: 0;
                        display: block;
                        z-index: 1;
                        position: absolute;
                        background-color: white;
                        @include fixedRoundedRectangle(200px,270px, 10px);
                        top: 30px;
                        left: math.div($profileSize, 2);
                        transform: translateX(-50%);
                        padding: math.div($profileSize, 2) + 25px 20px 10px;

                        .name{
                            display: flex;
                            justify-content: center;
                            font-weight: bolder;
                        }
                        .numInfo{
                            display: flex;
                            width: 100%;
                            justify-content: space-around;
                            
                            li{
                                cursor: pointer;
                                width: 50%;

                                &:hover{
                                    background-color: #f5f5f5;
                                    transition: .5s ease;
                                }

                                >div{
                                    display: flex;
                                    justify-content: center;

                                    &.top{
                                        font-size: 16px;
                                        font-weight: bolder;
                                    }
                                    &.bottom{
                                        font-size: 10px;
                                        font-weight: normal;
                                    }
                                }
                            }
                        }

                        .option{
                            margin-top: 15px;
                            li{
                                cursor: pointer;
                                font-weight: bold;
                                display: flex;
                                justify-content: space-between;
                                align-items: center;
                                
                                a{
                                    flex-grow: 1;
                                }

                                span{
                                    flex-grow: 1;
                                    margin-left: 10px;
                                }

                                &:hover{
                                    background-color: #f5f5f5;
                                    transition: .5s ease;
                                }

                                div,a{
                                    font-weight: inherit;
                                    color: black;
                                    display: flex;
                                    align-items: center;
                                    span{}

                                    img{
                                        @include fixedSquare(15px);
                                    }
                                }
                            }
                        }

                        hr{
                            margin: 10px 0px;
                        }
                    }
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
            .el-drawer__body{
                padding-top: 0px;

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

                        a{
                            @include flexCenter();
                            @include fullInParent();
                        }
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