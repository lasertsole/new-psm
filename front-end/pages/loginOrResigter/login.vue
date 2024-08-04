<template>
    <el-form class="login"
        :rules="rules"
        :model="userInfo"
    >
        <el-form-item prop="name">
            <el-input
                :maxlength="12"
                placeholder="用户名"
                v-model="userInfo.name"
                autocomplete="off"
                clearable
                style="margin-bottom: 0px;"
            />
        </el-form-item>

        <el-form-item prop="password">
            <el-input
                type="password"
                :maxlength="12"
                placeholder="密码"
                v-model="userInfo.password"
                autocomplete="off"
                clearable
                style="margin-bottom: 0px;"
            />
        </el-form-item>

        <el-form-item>
            <el-button
                type="primary"
                @click="submit()"
            >
                登录
            </el-button>
        </el-form-item>

        <div class="select">
            <NuxtLink to="register">注册新账号</NuxtLink>
            <span>忘记密码</span>
        </div>

    </el-form>
</template>

<script lang="ts" setup>
    import type { FormInstance, FormRules } from 'element-plus'

    definePageMeta({
        keepalive:true,
        pageTransition:{
            name: 'slide-right',
            mode: 'out-in',
        }
    })

    const ruleFormRef = ref<FormInstance>()

    type UserInfo = {
        name: string;
        password: string;
    };

    const userInfo:UserInfo = reactive({
        name: "",
        password: "",
    });

    let flagName = false;
    const validateName = (rule: any, value: any, callback: any) => {
        if (value == '') {
            callback(new Error('请输入用户名'));
            flagName = false;
        } else if (value.length < 3 || value.length > 12) {
            callback(new Error('无效用户名'));
            flagName = false;
        }
        else{
            flagName = true;
        }
    }

    let flagPass = true;
    const validatePass = (rule: any, value: string, callback: any) => {
        if (value == '') {
            callback(new Error('请输入密码'));
            flagPass = false;
        } else if (value.length < 6 || value.length > 16) {
            callback(new Error('无效密码'));
            flagPass = false;
        }
        else{
            if(userInfo.name != ""){
                flagPass = true;
            }
        }
    }

    const rules = reactive<FormRules<UserInfo>>({
        name: [{ validator: validateName, trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
    })

    function submit(){
        if(flagName&&flagPass){
            login(userInfo.name, userInfo.password);
        }
    }
</script>

<style lang="scss" scoped>
    .login{
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;

        :deep(.el-form-item){
            width: 100%;
            .el-input{
                height: 50px;
            }
        }

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