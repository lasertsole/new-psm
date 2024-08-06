<template>
    <ul class="tabBarArr">
        <li v-for="item in tabBarArr">
            <NuxtLink active-class="selected" class="tabbarChildItem" :to="item.path">{{item.name}}</NuxtLink>
        </li>
    </ul>
</template>

<script lang="ts" setup>
    const props = defineProps({tabBarArr:Object});
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