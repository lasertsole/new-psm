<template>
    <form class="register">
        <el-input :input-style="{lineHeight:'48px',minHeight:'48px'}" :maxlength="12" placeholder="手机号" v-model="userInfo.phone" clearable/>
        <el-input :input-style="{lineHeight:'48px',minHeight:'48px'}" :maxlength="12" placeholder="密码" type="password" v-model="userInfo.password" clearable/>
        <el-input :input-style="{lineHeight:'48px',minHeight:'48px'}" :maxlength="12" placeholder="确认密码" type="password" v-model="userInfo.repassword" clearable/>
        <div class="contract">请确认您已同意 <NuxtLink to="#">《喵剪辑服务协议》</NuxtLink></div>
        <el-button type="primary">注册</el-button>
        <NuxtLink to="login" class="backLogin">已有账号?点击登录</NuxtLink>
    </form>
</template>

<script lang="ts" setup>
    definePageMeta({
        keepalive:true,
        pageTransition:{
            name: 'slide-right',
            mode: 'out-in', 
        }
    })

    type UserInfo = {
        phone: string;
        password: string;
        repassword?:"";
    };

    const userInfo:UserInfo = reactive({
        phone: "",
        password: "",
        repassword:""
    });
</script>

<style lang="scss" scoped>
    .register{
        width: 100%;

        .contract{
            font-size: 14px;
            margin-bottom: 10px;
            a{
                color: #00a8e9;
            }
        }
        .backLogin{
            width: 100%;
            font-size: 14px;
            color: #00a8e9;
            display: flex;
            justify-content: center;
            cursor: pointer;
            >span{
                cursor: pointer;
            }
        }
    }

    .slide-left-enter-active,
    .slide-left-leave-active,
    .slide-right-enter-active,
    .slide-right-leave-active {
        transition: all 0.2s;
    }
    .slide-left-enter-from {
        opacity: 0;
        transform: translate(50px, 0);
    }
    .slide-left-leave-to {
        opacity: 0;
        transform: translate(-50px, 0);
    }
    .slide-right-enter-from {
        opacity: 0;
        transform: translate(-50px, 0);
    }
    .slide-right-leave-to {
        opacity: 0;
        transform: translate(50px, 0);
    }
</style>