<template>
    <form class="accountModify">
        <ul class="infoBox">
            <li class="profile">
                <div class="circle">
                    <accountInfoModifyUploadAvatar></accountInfoModifyUploadAvatar>
                </div>
            </li>
            
            <li>
                <div class="show">
                    <span class="optionName">昵称</span>
                    <div class="optionValue">
                        <el-input v-model="temptUserName" placeholder="请输入昵称" clearable :disabled="modifyIndex!=0"/>
                    </div>
                    <span class="modifyButton" @click="modifyIndex=0" v-show="modifyIndex!=0">修改</span>
                </div>
                <transition 
                    :css="false"
                    @enter="onEnter"
                    @leave="onLeave"
                >
                    <div class="hide" v-show="modifyIndex==0">
                        <button type="button" class="save" @click="modifyUserName()">保存</button>
                        <span class="cancel" @click="modifyIndex=-1">取消</span>
                    </div>
                </transition>
            </li>

            <li>
                <div class="show">
                    <span class="optionName">性别</span>
                    <div class="optionValue">
                        <el-radio-group v-model="temptSex" :disabled="modifyIndex!=1">
                            <el-radio :value="false">男</el-radio>
                            <el-radio :value="true">女</el-radio>
                        </el-radio-group>
                    </div>
                    <span class="modifyButton" @click="modifyIndex=1" v-show="modifyIndex!=1">修改</span>
                </div>
                <transition 
                    :css="false"
                    @enter="onEnter"
                    @leave="onLeave"
                >
                    <div class="hide" v-show="modifyIndex==1">
                        <button type="button" class="save" @click="modifySex()">保存</button>
                        <span class="cancel" @click="modifyIndex=-1">取消</span>
                    </div>
                </transition>
            </li>

            <li>
                <div class="show">
                    <span class="optionName">手机号</span>
                    <div class="optionValue">
                        <el-input v-model="temptUserPhoneNumber" placeholder="请输入手机号" clearable :disabled="modifyIndex!=2"/>
                    </div>
                    <span class="modifyButton" @click="modifyIndex=2" v-show="modifyIndex!=2">修改</span>
                </div>
                <transition 
                    :css="false"
                    @enter="onEnter"
                    @leave="onLeave"
                >
                    <div class="hide" v-show="modifyIndex==2">
                        <button type="button" class="save" @click="modifyUserPhoneNumber()">保存</button>
                        <span class="cancel" @click="modifyIndex=-1">取消</span>
                    </div>
                </transition>
            </li>

            <li>
                <div class="show">
                    <span class="optionName">邮箱</span>
                    <div class="optionValue">
                        <el-input v-model="temptUserEmail" placeholder="尚未绑定邮箱" clearable :disabled="modifyIndex!=3"/>
                    </div>
                    <span class="modifyButton" @click="modifyIndex=3" v-show="modifyIndex!=3">修改</span>
                </div>
                <transition
                    :css="false"
                    @enter="onEnter"
                    @leave="onLeave"
                >
                    <div class="hide" password v-show="modifyIndex==3">
                        <div>
                            <button type="button" class="save" @click="modifyUserEmail()">保存</button>
                            <span class="cancel" @click="modifyIndex=-1">取消</span>
                        </div>
                    </div>
                </transition>
            </li>
            
            <li v-if="userInfo.hasPass">
                <div class="show">
                    <span class="optionName">登录密码</span>
                    <div class="optionValue">
                        <el-input v-model="temptPassword" placeholder="请输入原来的登录密码" clearable type="password" :disabled="modifyIndex!=4"/>
                    </div>
                    <span class="modifyButton" @click="(modifyIndex=4)&&(temptPassword='')" v-show="modifyIndex!=4">修改</span>
                </div>
                <transition
                    :css="false"
                    @enter="onEnter"
                    @leave="onLeave"
                >
                    <div class="hide" password v-show="modifyIndex==4">
                        <div class="identifyPassword">
                            <span class="optionName">登录密码</span>
                            <el-input v-model="changePassword" placeholder="请输入新的登录密码" clearable type="password" v-show="modifyIndex==4"/>
                            
                        </div>
                        <div class="identifyPassword">
                            <span class="optionName"></span>
                            <el-input v-model="identifychangePassword" placeholder="确认新的登录密码" clearable type="password" v-show="modifyIndex==4"/>
                        </div>
                        <div>
                            <button type="button" class="save" @click="modifyUserPassword()">保存</button>
                            <span class="cancel" @click="(modifyIndex=-1)&&(temptPassword='111111')&&(changePassword='')&&(identifychangePassword='')">取消</span>
                        </div>
                    </div>
                </transition>
            </li>
        </ul>
    </form>
</template>
    
<script lang="ts" setup>
    import gsap from "gsap";
    import { ElMessage } from 'element-plus';
    import { watch } from "vue";


    /**以下为展开合上盒子时的动画特效**/
    /*详情盒子动画钩子*/
    let animate:any = undefined;//gsap动画容器
    let animateLock:boolean = false;//动画锁

    function onEnter(el:any, done:Function):void{
        animate = gsap.to(el,{
            opacity:1,
            duration: .3,//持续时间
            onStart:()=>{//开始触发函数
                animateLock=true;
            },
            onComplete:()=>{//结束触发函数
                done();
                animateLock=false;
            }
        });
    }

    function onLeave(el:any, done:Function):void{
        animate = gsap.to(el,{
            opacity:0,
            duration: 0,//持续时间
            onStart:()=>{//开始触发函数
                animateLock=true;
            },
            onComplete:()=>{//结束触发函数
                done();
                animateLock=false;
            }
        });
    }
    /**以上为展开合上盒子时的动画特效**/


    // /**以下为修改信息部分**/
    const temptUserName = ref<string>(userInfo.name||"");//临时用户名
    const temptSex = ref<boolean | undefined>(userInfo.sex!=undefined?userInfo.sex:undefined);//临时用户名
    const temptUserPhoneNumber = ref<string>(userInfo.phone||"");//临时用户手机号
    const temptUserEmail = ref<string>(userInfo.email||"");
    const temptPassword = ref<string>("111111");//临时旧密码
    const changePassword = ref<string>("");//新密码
    const identifychangePassword = ref<string>("");//确认新密码
    const modifyIndex = ref<number>(-1);//展开显示索引

    const modifyUserName = debounce(async ()=>{
        if(temptUserName.value.length == 0){
            ElMessage.error("名字不能为空");  
        }
        else if(temptUserName.value.length<3||temptUserName.value.length>12){
            ElMessage.error("请输入大于等于3且小于等于12长度的名字");   
        }
        else if(temptUserName.value == userInfo.name){
            ElMessage.error("修改后的名字不能与原来的一样");
        }
        else{
            let isOk = await updateAccountInfo({name:temptUserName.value});
            if(isOk){
                temptUserName.value = userInfo.name||"未设置";
                modifyIndex.value = -1;
            }
        }
    }, 1000);

    const modifySex = debounce(async ()=>{
        if(temptSex.value == undefined){
            ElMessage.error("性别未选择");  
        }
        else{
            let isOk = await updateAccountInfo({sex:temptSex.value});
            if(isOk){
                temptSex.value = userInfo.sex;
                modifyIndex.value = -1;
            }
        }
    }, 1000);

    const modifyUserPhoneNumber = debounce(async ()=>{
        if(temptUserPhoneNumber.value.length == 0){
            ElMessage.error('手机号不能为空');
        }
        else if(!/^1[0-9]{10}$/.test(temptUserPhoneNumber.value)){
            ElMessage.error('无效手机号');
        }
        else if(temptUserPhoneNumber.value == userInfo.phone){
            ElMessage.error("修改后的手机号不能与原来的一样");
        }
        else{
            let isOk = await updateAccountInfo({phone:temptUserPhoneNumber.value});
            if(isOk){
                temptUserPhoneNumber.value = userInfo.phone||"";
                modifyIndex.value = -1;
            }
        }
    }, 1000);

    const modifyUserEmail = debounce(async ()=>{
        const regex = new RegExp("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
        
        if(temptUserEmail.value.length == 0){
            ElMessage.error('邮箱不能为空');
        }
        else if(temptUserEmail.value.length < 8 || temptUserEmail.value.length > 255 || !regex.test(temptUserEmail.value)){
            ElMessage.error('无效邮箱');
        }
        else{
            let isOk = await updateAccountInfo({email:temptUserEmail.value});
            if(isOk){
                temptUserEmail.value = userInfo.email||"";
                modifyIndex.value = -1;
            }
        }
    })

    const modifyUserPassword = debounce(async ()=>{
        if(temptPassword.value.length == 0){
            ElMessage.error('旧密码不能为空');
        }
        else if(temptPassword.value.length<8){
            ElMessage.error('旧密码过短');
        }
        else if(temptPassword.value.length>=26){
            ElMessage.error('旧密码过长');
        }
        else if(changePassword.value.length == 0){
            ElMessage.error('新密码不能为空');
        }
        else if(changePassword.value.length<8){
            ElMessage.error('新密码过短');
        }
        else if(changePassword.value.length>=26){
            ElMessage.error('新密码过长');
        }
        else if(changePassword.value != identifychangePassword.value){
            ElMessage.error('新密码和确认密码不一致');
        }
        else if(temptPassword.value == changePassword.value){
            ElMessage.error('新旧密码不能一样');
        }
        else{
            let isOk = await updatePassword(temptPassword.value, changePassword.value);
            if(isOk){
                temptPassword.value = "111111";
                modifyIndex.value = -1;
            }
        }
    }, 1000);
    /**以上为修改信息部分**/
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    .accountModify{
        @include fullInParent();
        background-color: #e6eaee;
        display: flex;
        justify-content: center;
        align-items: center;

        .infoBox{
            @include fixedWidth(500px);
            min-width: 350px;
            background-color: white;
            border-radius: 7px;
            display: flex;
            flex-direction: column;

            li{
                padding: 30px;
                &:not(:last-child,:first-child){
                    border-bottom: 1px solid #ececec;
                }

                &.profile{
                    display: flex;
                    justify-content: center;
                    padding: 0px;
                    position: relative;
                    height: 50px;
                
                    .circle{
                        position: absolute;
                        @include fixedCircle(100px);
                        top: -40px;
                    }
                }

                .show{
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    font-size:12px;

                    .optionName{
                        min-width: 20%;
                        font-size: 14px;
                    }

                    .optionValue{
                        flex-grow: 1;
                    }

                    .modifyButton{
                        display: inline-block;
                        color: #00a8e9;
                        cursor: pointer;
                    }
                }

                .hide{
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    margin-top: 10px;
                    opacity: 0;

                    &[password]{
                        flex-direction: column;
                        
                        .identifyPassword{
                            display: flex;
                            width: 100%;
                            justify-content: flex-start;
                            margin-top: 0px;

                            .optionName{
                                min-width: 20%;
                                font-size: 14px;
                                opacity: 0;
                            }
                        }
                    }

                    &.collasp{
                        display: none !important;
                    }

                    .save{
                        @include fixedWidth(150px);
                        background-color: #00a8e9;
                        color: white;
                        border-radius: 4px;
                        font-size: 18px;
                        padding: 7px 24px;
                        outline: none;
                        border: none;
                        cursor: pointer;

                        &:hover{
                            background-color: #33b9ed;
                            transition: .3s linear;
                        }
                    }

                    .cancel{
                        color: #00a8e9;
                        padding: 12px 10px;
                        font-size: 14px;
                        cursor: pointer;
                    }
                }

                &::v-deep(.el-input){
                    @include fixedRetangle(200px, 21px);
                    
                    &:not(:first-child){
                        margin-bottom: 10px;
                    }

                    &.is-disabled{
                        .el-input__wrapper{
                            background-color: inherit;
                            box-shadow: none;
                        }
                    }
                }
            }
        }
    }
</style>