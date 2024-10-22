export default defineNuxtRouteMiddleware(
    (to,from)=>
    {
        // 在服务器端跳过中间件
        if (process.server) return;
        if(to.path == "/model3DShow"){
            return navigateTo("/model3DShow/all");
        }
    }
);