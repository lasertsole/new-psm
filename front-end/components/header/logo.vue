<template>
    <div class="logo">
        <div class="logo-image"></div>
        <div class="logo-text">
            <h2>{{config.public.appName}}</h2>
            <h5>{{config.public.appDomainName}}</h5>
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { useRuntimeConfig } from '#app';
    const config = useRuntimeConfig();
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    .logo{
        height: 100%;
        display: flex;
        flex-direction: row;
        align-items: center;
        margin-right: 10px;
        color: #fb7299;
        &:hover{
            color: #fb7299
        }
        &-image{
            @include fixedSquare(80px);
            background-image: url(/images/psmlogo.png);
            background-repeat: no-repeat;
            background-position: center 5px;
            background-size: 90px;
            margin-right: 5px;
        }
        &-text{
            height: 100%;
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            >*{
                display: flex;
                justify-content: center;
            }
            h2{
                margin-top: 10px;
                font-size: 28px;
                line-height: 26px;
                letter-spacing:8px;
                text-indent: 8px;
            }

            h5{
                font-size: 13.5px;
                letter-spacing:1px;
            }
        }
    }
    @media screen and (max-width: 800px) and (min-width: 600px) {
        .logo{
            display: none;
        }
    }
    @media screen and (max-width: 600px) {
        .logo{
            &-image{
                @include fixedSquare(50px);
                background-size: 56px;
                background-position: center 4px;
            }
            &-text{
                h2{
                    font-size: 17px;
                    letter-spacing:2.5px;
                    text-indent: 0px;
                    line-height: 20px;
                }

                h5{
                    font-size: 7.5px;
                    letter-spacing:0.5px;
                }
            }
        }
    }
</style>