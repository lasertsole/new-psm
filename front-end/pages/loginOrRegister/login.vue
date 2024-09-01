<template>
    <el-form class="login"
        :rules="rules"
        :model="userInfo"
        ref="ruleFormRef"
    >
        <el-form-item prop="name">
            <el-input
                :maxlength="12"
                placeholder="用户名"
                v-model="userInfo.name"
                autocomplete="off"
                clearable
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
            />
        </el-form-item>

        <el-form-item>
            <el-button
                type="primary"
                @click="submit(ruleFormRef)"
            >
                登录
            </el-button>
        </el-form-item>

        <div class="select">
            <NuxtLink to="register">注册新账号</NuxtLink>
            <span>忘记密码</span>
        </div>

        <div class="third-parth">
            <div class="third-parth-line">
                <div class="left-line"></div>
                <span>第三方登录</span>
                <div class="right-line"></div>
            </div>

            <div class="third-parth-list">
                <svg @click="githubLogin()"
                    height="32" aria-hidden="true" viewBox="0 0 24 24" version="1.1" width="32" data-view-component="true" class="octicon octicon-mark-github v-align-middle color-fg-default">
                    <path d="M12.5.75C6.146.75 1 5.896 1 12.25c0 5.089 3.292 9.387 7.863 10.91.575.101.79-.244.79-.546 0-.273-.014-1.178-.014-2.142-2.889.532-3.636-.704-3.866-1.35-.13-.331-.69-1.352-1.18-1.625-.402-.216-.977-.748-.014-.762.906-.014 1.553.834 1.769 1.179 1.035 1.74 2.688 1.25 3.349.948.1-.747.402-1.25.733-1.538-2.559-.287-5.232-1.279-5.232-5.678 0-1.25.445-2.285 1.178-3.09-.115-.288-.517-1.467.115-3.048 0 0 .963-.302 3.163 1.179.92-.259 1.897-.388 2.875-.388.977 0 1.955.13 2.875.388 2.2-1.495 3.162-1.179 3.162-1.179.633 1.581.23 2.76.115 3.048.733.805 1.179 1.825 1.179 3.09 0 4.413-2.688 5.39-5.247 5.678.417.36.776 1.05.776 2.128 0 1.538-.014 2.774-.014 3.162 0 .302.216.662.79.547C20.709 21.637 24 17.324 24 12.25 24 5.896 18.854.75 12.5.75Z"></path>
                </svg>
            </div>
        </div>

    </el-form>
</template>

<script lang="ts" setup>
    import type { FormInstance, FormRules } from 'element-plus'
    import type { UserInfo } from "@/types/user";
    import { useRouter } from '#app'

    definePageMeta({
        keepalive:true,
        pageTransition:{
            name: 'slide-right',
            mode: 'out-in',
        }
    });

    const ruleFormRef = ref<FormInstance>();

    const userInfo:UserInfo = reactive({
        name: "",
        password: "",
    });

    const validateName = (rule: any, value: any, callback: any) => {
        const regex = new RegExp("^[\u4e00-\u9fa5a-zA-Z0-9_]+$");

        if (value == '') {
            callback(new Error('请输入用户名'));
        } else if (value.length < 3 || value.length > 12 || !regex.test(value)) {
            callback(new Error('无效用户名'));
        }
        else{
            callback();
        }
    }

    const validatePass = (rule: any, value: string, callback: any) => {
        const regex = new RegExp("^[a-zA-Z0-9_*]+$");

        if (value == '') {
            callback(new Error('请输入密码'));
        } else if (value.length < 8 || value.length > 26 || !regex.test(value)) {
            callback(new Error('无效密码'));
        }
        else{
            callback();
        }
    }

    const rules = reactive<FormRules<UserInfo>>({
        name: [{ validator: validateName, trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
    })

    const { $emit }= useNuxtApp();
    const router = useRouter();
    const submit = debounce((formEl: FormInstance | undefined)=>{
        if (!formEl) return;
        formEl.validate((valid) => {
            if (valid) {
                login(userInfo.name, userInfo.password).then(isSuccuss => {
                    if(isSuccuss){
                        $emit("online");
                        router.push("/");
                    }
                });
            }
        })
    }, 1000);

    // github登录
    const githubLogin = debounce(()=>{
        thirdPartyLogin("/giteeLogin");
    }, 1000);
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
                margin-bottom: 0px !important;
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

        .third-parth{
            width: 100%;
            
            &-line{
                margin: 10px 0px;
                width: 100%;
                display: flex;
                align-items: center;
                .left-line{
                    flex-grow: 1;
                    height: 1px;
                    background-color: rgb(182, 182, 182);
                }
                .right-line{
                    flex-grow: 1;
                    height: 1px;
                    background-color: rgb(182, 182, 182);
                }
                span{
                    color: rgb(182, 182, 182);
                }
            }

            &-list{
                width: 100%;
                display: flex;
                justify-content: center;

                svg{
                    cursor: pointer;
                }
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