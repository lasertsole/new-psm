<template>
  <NuxtPwaManifest />
  <div>
    <NuxtLayout>
      <NuxtPage keepalive/>
    </NuxtLayout>
  </div>
</template>

<style lang="scss">
  .el-loading-mask{z-index: 1 !important;}
</style>

<script lang="ts" setup>  
  // 这里的代码仅在客户端执行
  // 配置登录事件
  onMounted(async ()=>{
    if(localStorage.getItem("token")){
      // 确保所有子组件都渲染完毕
      
      // 延时执行(加入事件循环中)
      setTimeout(async ()=>{
        fastLogin();
      }, 0);
    };
  });
  
  let DMServiceInstance: DMService; // 一对一聊天服务
  let RTCServiceInstance: RTCService;
  on("online", ()=>{
    DMServiceInstance = DMService.getInstance();
    RTCServiceInstance = RTCService.getInstance();
  });

  on("offline", ()=>{
    // 销毁实例
    DMService.destroyInstance();
    RTCService.destroyInstance();
    
    // 断开所有连接
    wsManager._close();
  });

  // 组件销毁时移除监听
  onBeforeUnmount(() => {
  });
</script>