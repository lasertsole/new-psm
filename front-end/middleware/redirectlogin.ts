export default defineNuxtRouteMiddleware(
    (to,from)=>
    {
        // 在服务器端跳过中间件
        if (process.server) return;
        // localStorage.getItem("online")&&navigateTo("/");
        if(to.path == "/loginOrResigter"){
            return navigateTo("/loginOrResigter/login");
        }
        else if(to.path == "/subtitles"){
            return navigateTo("/subtitles/all");
        }
    }
);