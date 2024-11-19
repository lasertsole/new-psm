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
  let DMServiceInstance; // 一对一聊天服务

  // 这里的代码仅在客户端执行
  // 配置登录事件
  onMounted(async ()=>{
    if(localStorage.getItem("token")){
      // 确保所有子组件都渲染完毕
      
      // 延时执行(加入事件循环中)
      setTimeout(async ()=>{
        fastLogin();
      }, 0);
    }
  });
  
  on("online", ()=>{
    DMServiceInstance = DMService.getInstance();
  });

  // 组件销毁时移除监听
  onBeforeUnmount(() => {
  });
</script>