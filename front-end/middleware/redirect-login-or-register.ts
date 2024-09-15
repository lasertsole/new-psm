export default defineNuxtRouteMiddleware(
    (to,from)=>
    {
        // 在服务器端跳过中间件
        if (process.server) return;
        if(userInfo.isLogin){
            return navigateTo("/")
        }
        if(to.path == "/loginOrRegister"){
            return navigateTo("/loginOrRegister/login");
        }
        else if(to.path == "/subtitles"){
            return navigateTo("/subtitles/all");
        }
    }
);