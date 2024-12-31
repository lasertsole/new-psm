<template>
  <div class="thirdLogin">
    <div class="centerBox">
      <div class="icon"></div>
      <div class="text">登录成功</div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  @use "sass:math";
  @use "@/common.scss" as common;

  .thirdLogin{
    @include common.fixedRetangle(100%, 200px);
    @include common.flexCenter();

    .centerBox{
      @include common.flexCenter();
      flex-direction: column;

      .icon{
        $iconSize: 50px;
        @include common.fixedSquare($iconSize);
        background-size: $iconSize;
        background-position: center;
        background-image: url(/icons/loginSuccuss.svg);
        background-repeat: no-repeat;
      }

      .text{
        font-size: 20px;
        margin-top: 10px;
      }
    }
  }
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
    localStorage.setItem("token", getCookie("token") || "");
    deleteCookie("token");
    // 快速登录,获取当前用户信息
    setTimeout(async ()=>{
        let isSuccess:boolean = await fastLogin();
        // 快速登录成功
        if(isSuccess){
          $emit("online");
        };
      }, 0);
  })

  definePageMeta({
      name: "thirdLogin"
  });
</script>