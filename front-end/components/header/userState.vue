<template>
     <!-- 登录注册按钮 -->
     <div v-if="!isOnline" class="loginOrRegister">
        <NuxtLink to="/loginOrResigter/login" class="login">登录</NuxtLink>
        <NuxtLink to="/loginOrResigter/register" class="register">注册</NuxtLink>
    </div>
    <div class="userTool" v-else @click="drawer = true"></div>
    <el-drawer
        v-model="drawer"
        :direction="direction"
        :modal="true"
    >
        <pre>
            1
            1
            1
            1
            1
            1
            1

            1
            1
            1
            1
            1
            1
            1
            1
            1

            1
            1
            1
            1
            1
            1
            1
            1
            1
        </pre>
    </el-drawer>
</template>

<script lang="ts" setup>
    import type { DrawerProps } from 'element-plus'
    const isOnline = ref<boolean>(true);
    const { $on } = useNuxtApp();

    $on("online", () => {
        isOnline.value = true;
    });

    const drawer = ref(false)
    const direction = ref<DrawerProps['direction']>('ttb')
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