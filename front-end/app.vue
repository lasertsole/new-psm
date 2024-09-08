<template>
  <div>
    <NuxtLayout>
      <NuxtPage/>
    </NuxtLayout>
  </div>
</template>

<style lang="scss">
</style>

<script lang="ts" setup>
  const { $on }= useNuxtApp();
  import {onMounted} from "vue"

  // 这里的代码仅在客户端执行
  onMounted(()=>{
    // localStorage.setItem("online", "false");
    userInfo.isLogin = false;

    // 延时执行(加入事件循环的大事件中)
    setTimeout(()=>{
      // 快速登录,获取当前用户信息
      fastLogin();
    }, 0);
  });
</script>