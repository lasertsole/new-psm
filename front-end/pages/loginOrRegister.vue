<template>
    <div class="loginOrResigter">
        <div class="box">
            <div class="logo">
                <div class="logo-image"></div>
                <div class="logo-text">
                    <h2>喵字幕</h2>
                    <h5>miaozimu.com</h5>
                </div>
            </div>

            <NuxtPage keepalive></NuxtPage>
        </div>
    </div>
</template>

<script setup lang="ts">
    definePageMeta({
        middleware:["redirect-login-or-register"]
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .loginOrResigter{
        width: 100%;
        height: 100%;
        overflow-y: auto;
        background-color: rgb(230,234,238);
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        .box{
            box-sizing: border-box;
            padding: 60px 50px;
            background-color: white;
            margin-top: 60px;
            width: 450px;
            border-radius: 8px;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            align-items: center;
            .logo{
                margin-bottom: 50px;
                display: flex;
                flex-direction: row;
                align-items: center;
                &-image{
                    @include fixedSquare(60px);
                    background-image: url(/images/psmlogo.png);
                    background-size: 100%;
                    margin-right: 10px;
                    background-position: center;
                }
                
                &-text{
                    h2{
                        font-size: 22px;
                        color: #fb7299;
                    }

                    h5{
                        font-size: 11.5px;
                        color: #fb7299;
                    }
                }
            }

            :deep(.el-input){
                margin-bottom: 15px;
            }

            :deep(.el-button){
                width: 100%;
                box-sizing: border-box;
                height: 34px;
                margin-bottom: 15px;
            }
        }
    }
</style>