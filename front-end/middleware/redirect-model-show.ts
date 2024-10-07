export default defineNuxtRouteMiddleware(
    (to,from)=>
    {
        // 在服务器端跳过中间件
        if (process.server) return;
        if(to.path == "/modelShow"){
            return navigateTo("/modelShow/index");
        }
    }
);