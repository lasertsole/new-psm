<template>
</template>

<style lang="scss">
</style>

<script lang="ts" setup>
  import { useSSRContext } from 'vue';
  const { $emit }= useNuxtApp();

  let cookie: string | undefined;

  // 获取 cookie
  if(process.server){
    const { event } = useSSRContext();
    cookie = event?.req?.headers?.cookie;
  }else{
    cookie = document.cookie;
  }

  // 解析 cookie
  function parseCookie(): Record<string, string> {
    if (!cookie) return {};
  
    const cookieObj = cookie?.split('; ').reduce((acc, cookie) => {
      const [name, value] = cookie.split('=');
      acc[name.trim()] = decodeURIComponent(value);
      return acc;
    }, {} as Record<string, string>);
  
    return cookieObj;
  }

  // 获取 cookie内键的值
  function getCookie(name: string): string | undefined {
    const cookies = parseCookie();
    return cookies[name];
  }

  // 删除 cookie
  function deleteCookie(name: string): void {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
  }

  // 登录成功
  onMounted(()=>{
    localStorage.setItem("online", "true");
    localStorage.setItem("token", getCookie("token") || "");
    deleteCookie("token");
    // 快速登录,获取当前用户信息
    fastLogin();

    $emit("online");
    navigateTo("/");
  })
</script>