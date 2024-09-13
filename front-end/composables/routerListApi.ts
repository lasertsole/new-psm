import type { Router } from "@/types/router";

export const routerList:Router[] = [
    {
        name: '首页',
        path: '/',
        needOnLine: false,
        needOffLine: false,
        tarbar: true,
    },
    {
        name: '字幕',
        path: '/subtitles',
        needOnLine: false,
        needOffLine: false,
        tarbar: true,
    },
    {
        name: '企划',
        path: '/#',
        needOnLine: false,
        needOffLine: false,
        tarbar: true,
    },
    {
        name: '作品',
        path: '/#',
        needOnLine: false,
        needOffLine: false,
        tarbar: true,
    },
    {
        name: '登录',
        path: '/loginOrRegister/login',
        needOnLine: false,
        needOffLine: true,
        tarbar: false,
    },
    {
        name: '注册',
        path: '/loginOrRegister/register',
        needOnLine: false,
        needOffLine: true,
        tarbar: false,
    },
]

export function getRouterList():Router[]{
    return routerList;
}