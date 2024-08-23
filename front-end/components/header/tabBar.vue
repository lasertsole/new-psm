<template>
    <ul class="tabBarArr">
        <li v-for="item in routerList">
            <NuxtLink active-class="selected" class="tabbarChildItem" :to="item.path">{{item.name}}</NuxtLink>
        </li>
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
                color: #707070;
                &.selected{
                    color: #f87299;
                }
            }
        }
    }
</style>