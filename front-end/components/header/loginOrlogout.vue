<template>
     <!-- 登录注册按钮 -->
     <div v-if="!isOnline" class="loginOrRegister">
        <router-link to="#" class="login">登录</router-link>
        <router-link to="#" class="register">注册</router-link>
    </div>
    <ul class="userTool" v-else>
        111
    </ul>
</template>

<script lang="ts" setup>
    const isOnline = ref<boolean>(false);
</script>

<style lang="scss">
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
                .userProfile{
                    @include fixedCircle($profileSize);
                    z-index: 2;
                    position: relative;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    transition: linear .3s;

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
                    @include fixedRoundedRectangle(200px,280px, 10px);
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
    @media screen and (max-width: 600px) {
        .loginOrRegister{
            display: none;
        }
        .userTool{
            display: none;
        }
    }
</style>