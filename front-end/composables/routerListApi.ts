import type { Router } from "@/types/router";

export const routerList:Router[] = [
    {
        name: '首页',
        path: '/',
        needOnLine: false,
        needOffLine: false,
    },
    {
        name: '橱窗',
        path: '/#',
        needOnLine: false,
        needOffLine: false,
    },
    {
        name: '企划',
        path: '/#',
        needOnLine: false,
        needOffLine: false,
    },
    {
        name: '作品',
        path: '/#',
        needOnLine: false,
        needOffLine: false,
    },
    {
        name: '登录',
        path: '/login',
        needOnLine: false,
        needOffLine: true,
    },
    {
        name: '注册',
        path: '/register',
        needOnLine: false,
        needOffLine: true,
    },
]

export function getRouterList():Router[]{
    return routerList;
}