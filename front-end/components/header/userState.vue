<template>
     <!-- 登录注册按钮 -->
     <div v-if="!isOnline" class="loginOrRegister">
        <NuxtLink to="/loginOrRegister/login" class="login">登录</NuxtLink>
        <NuxtLink to="/loginOrRegister/register" class="register">注册</NuxtLink>
    </div>
    <div class="userTool" @click="drawer = true"></div>
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
                    v-if="!(item.tarbar==true&&isOnline)"
                >
                    {{item.name}}
                </li>
            </template>
        </ul>
    </el-drawer>
</template>

<script lang="ts" setup>
    import type { DrawerProps } from 'element-plus'
    import type { Router } from '@/types/router';

    const isOnline = ref<boolean>(false);
    const { $on } = useNuxtApp();

    $on("online", () => {
        isOnline.value = true;
    });

    const drawer = ref(false)
    const direction = ref<DrawerProps['direction']>('ttb')

    const routerList:Router[] = getRouterList();
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
    $showDraw: none;
    @media screen and (max-width: 600px) {
        .loginOrRegister{
            display: none;
        }
        .userTool{
            display: block;
        }
    }
</style>