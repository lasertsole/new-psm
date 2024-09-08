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
  const { $emit } = useNuxtApp();

  // 这里的代码仅在客户端执行
  onMounted(()=>{
    // localStorage.setItem("online", "false");
    userInfo.isLogin = false;
    
    // 延时执行(加入事件循环中)
    setTimeout(()=>{
      let isSuccess:boolean = fastLogin();
      // 快速登录成功
      if(isSuccess){
        $emit("online");
      };
    }, 0);
  });
</script>