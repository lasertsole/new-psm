<template>
    <el-form class="register"
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
                :maxlength="26"
                placeholder="密码"
                type="password"
                v-model="userInfo.password"
                autocomplete="off"
                clearable
            />
        </el-form-item>
        <el-form-item prop="repassword">
            <el-input
                :maxlength="26"
                placeholder="确认密码"
                type="password"
                v-model="userInfo.repassword"
                autocomplete="off"
                clearable
            />
        </el-form-item>
        <!-- <div class="contract">请确认您已同意 <NuxtLink to="#">《喵剪辑服务协议》</NuxtLink></div> -->
        <el-form-item prop="email">
            <el-input
                :maxlength="254"
                placeholder="电子邮箱"
                v-model="userInfo.email"
                autocomplete="off"
                clearable
            />
        </el-form-item>
        <el-form-item>
            <el-button
                type="primary"
                @click="submit(ruleFormRef)"
            >
                注册
            </el-button>
        </el-form-item>
        <NuxtLink to="login" class="backLogin">已有账号?点击登录</NuxtLink>
    </el-form>
</template>

<script lang="ts" setup>
    import type { FormInstance, FormRules } from 'element-plus'
    import type { UserInfo } from "@/types/user";

    definePageMeta({
        keepalive:true,
        pageTransition:{
            name: 'slide-right',
            mode: 'out-in', 
        }
    });

    const ruleFormRef = ref<FormInstance>();

    //类型继承与属性覆盖
    type UserDetailInfo = UserInfo &{
        email: string
    }

    const userInfo:UserDetailInfo = reactive({
        name: "",
        password: "",
        repassword: "",
        email: ""
    });

    const validateName = (rule: any, value: any, callback: any) => {
        const regex = new RegExp("^[\u4e00-\u9fa5a-zA-Z0-9_]+$");

        if (value == '') {
            callback(new Error('请输入用户名'));
        }
        else if (value.length < 3 || value.length > 12 || !regex.test(value)) {
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
        }
        else if (value.length < 8 || value.length > 26 || !regex.test(value)) {
            callback(new Error('无效密码'));
        }
        else{
            ruleFormRef&&ruleFormRef.value&&ruleFormRef.value.validateField('repassword')
            callback();
        }
    }

    const validateRepass = (rule: any, value: string, callback: any) => {
        const regex = new RegExp("^[a-zA-Z0-9_*]+$");

        if (value == '') {
            callback(new Error('请输入密码'));
        }
        else if (value.length < 8 || value.length > 26 || !regex.test(value)) {
            callback(new Error('无效密码'));
        }
        else if(value != userInfo.password){
            callback(new Error('两次密码不一致'));
        }
        else{
            callback();
        }
    }

    const validateEmail = (rule: any, value: string, callback: any) => {
        const regex = new RegExp("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
        
        if (value == '') {
            callback(new Error('请输入电子邮箱'));
        }
        else if (value.length < 8 || value.length > 255 || !regex.test(value)) {
            callback(new Error('无效电子邮箱'));
        } 
        else{
            callback();
        }
    }

    const rules = reactive<FormRules<UserInfo>>({
        name: [{ validator: validateName, trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
        repassword: [{ validator: validateRepass, trigger: 'blur' }],
        email: [{ validator: validateEmail, trigger: 'blur' }],
    })
    
    const { $emit }= useNuxtApp();
    const router = useRouter();
    const submit = debounce((formEl: FormInstance | undefined)=>{
        if (!formEl) return
        formEl.validate((valid) => {
            if (valid) {
                if(userInfo.name&&userInfo.password&&userInfo.email){
                    register(userInfo.name, userInfo.password, userInfo.email).then(isSuccuss => {
                        if(isSuccuss){
                            $emit("online");
                            router.push("/");
                        }
                    });
                }
            }
        })
    }, 1000);
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

        :deep(.el-form-item){
            width: 100%;
            .el-input{
                height: 50px;
                margin-bottom: 0px !important;
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