const needAuthPages: string[] = [// 示例需要认证的页面路径的正则表达式数组
    "/home"
    ,"/model/3D/detailShow/.*"
    ,"/model/3D/show/follow"
    ,"/message"
];

// 将多条正则表达式转换为一条正则表达式
const needAuthRegex = new RegExp(`^(${needAuthPages.join('|')})$`);

export default defineNuxtRouteMiddleware(
    (to,from)=>
    {
        // 在服务器端跳过中间件
        if (import.meta.server) return;
        if(to.path == "/"&&userInfo.isLogin){
            return navigateTo("/home");
        }
        else if(needAuthRegex.test(to.path)&&!userInfo.isLogin){
            ElMessage.warning("此页面需要登录");
            return navigateTo("/");
        }
    }
);