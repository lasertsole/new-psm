export default defineNuxtRouteMiddleware(
    (to,from)=>
    {
        if(to.path == "/loginOrResigter"|| to.path == "/loginOrResigter/"){
            return navigateTo("/loginOrResigter/login");
    }
});