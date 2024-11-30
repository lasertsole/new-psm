import type { Router } from "@/types/router";

export const routerList:Router[] = [
    {
        name: '首页',
        path: '/',
        needOnLine: false,
        needOffLine: true,
        tarbar: true,
    },
    {
        name: '3D',
        path: '/model/3D/show',
        needOnLine: false,
        needOffLine: false,
        tarbar: true,
    },
    {
        name: '应用',
        path: '/application',
        needOnLine: false,
        needOffLine: false,
        tarbar: true,
    },
    {
        name: '消息',
        path: '/message',
        needOnLine: false,
        needOffLine: false,
        tarbar: true,
    },
    {
        name: '登录',
        path: '/login',
        needOnLine: false,
        needOffLine: true,
        tarbar: false,
    },
    {
        name: '注册',
        path: '/register',
        needOnLine: false,
        needOffLine: true,
        tarbar: false,
    },
]

export function getRouterList():Router[]{
    return routerList;
}