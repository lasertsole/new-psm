<template>
  <NuxtPwaManifest />
  <div class="relative">
    <NuxtLayout>
      <NuxtPage keepalive/>
    </NuxtLayout>
    <RtcInvitationArr></RtcInvitationArr>
  </div>
</template>

<style lang="scss">
  .el-loading-mask{z-index: 1 !important;}
</style>

<script lang="ts" setup>
import { initWebWorker } from '@/composables/webWorker';

  
  // 这里的代码仅在客户端执行
  // 配置登录事件
  onMounted(async ()=>{
    if(localStorage.getItem("token")){
      // 确保所有子组件都渲染完毕
      
      // 延时执行(加入事件循环中)
      setTimeout(async ()=>{
        fastLogin();// 快速登录，因为要使用localstorage，所以必须客户端渲染
      }, 0);
    };
  });
  
  let SecurityInstance: SecurityService;// 安全服务
  let DMServiceInstance: DMService; // 一对一聊天服务
  let RTCServiceInstance: RTCService; // RTC实时交互服务
  on("online", async ():Promise<void>=>{
    wsManager.open();
    SecurityInstance = await SecurityService.getInstance();

    DMServiceInstance = await DMService.getInstance();
    // 初始化私信
    DMServiceInstance.initDM();
    
    RTCServiceInstance = await RTCService.getInstance();

    initWebWorker();
  });

  on("offline", ()=>{
    // 销毁实例
    DMService.destroyInstance();
    RTCService.destroyInstance();
    SecurityService.destroyInstance();
    
    // 断开所有连接
    wsManager._close();
  });

  // 组件销毁时移除监听
  onBeforeUnmount(() => {
  });
</script>