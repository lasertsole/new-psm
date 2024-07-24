<template>
    <form class="login">
        <el-input :input-style="{lineHeight:'48px',minHeight:'48px'}" :maxlength="12" placeholder="手机号" v-model="userInfo.phone" clearable/>
        <el-input :input-style="{lineHeight:'48px',minHeight:'48px'}" :maxlength="12" placeholder="密码" v-model="userInfo.password" clearable/>
        <el-button type="primary" @click="submit()">登录</el-button>
        <div class="select">
            <NuxtLink to="register">注册新账号</NuxtLink>
            <span>忘记密码</span>
        </div>
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
    });

    function submit(){
        const { data, error, refresh } = useApiFetch("userState/test");
        alert(data);
    }
</script>

<style lang="scss" scoped>
    .login{
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        .select{
            width: 100%;
            font-size: 14px;
            color: #00a8e9;
            display: flex;
            justify-content: space-between;
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