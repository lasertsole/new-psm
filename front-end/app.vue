<template>
  <NuxtPwaManifest />
  <div>
    <NuxtLayout>
      <NuxtPage keepalive/>
    </NuxtLayout>
  </div>
</template>

<style lang="scss">
</style>

<script lang="ts" setup>
  const { $emit } = useNuxtApp();

  // 这里的代码仅在客户端执行
  onMounted(()=>{
    userInfo.isLogin = false;
    
    if(localStorage.getItem("token")){
      // 延时执行(加入事件循环中)
      setTimeout(async ()=>{
        let isSuccess:boolean = await fastLogin();
        // 快速登录成功
        if(isSuccess){
          $emit("online");
        };
      }, 0);
    }
  });
</script>