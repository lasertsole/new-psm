<template>
    <ul class="tabBarArr">
        <template  v-for="item in routerList">
            <li>
                <NuxtLink active-class="selected" class="tabbarChildItem" :to="item.path">{{item.name}}</NuxtLink>
            </li>
        </template>
    </ul>
</template>

<script lang="ts" setup>
    import type { Router } from '@/types/router';

    const routerList:Router[] = getRouterList().filter((item) => {
        return item.tarbar == true;
    });
</script>

<style lang="scss" scoped>
    .tabBarArr{
        display: flex;
        flex-direction: row;
        font-size: 18px;
        align-items: center;

        li{
            width: 80px;
            display: flex;
            justify-content: center;
            align-items: center;
            a{
                display: flex;
                justify-content: center;
                align-items: center;
                color: #707070;
                
                &.selected{
                    color: #4091ff;
                }
            }
        }
    }
</style>