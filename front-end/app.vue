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
  const { $emit, $on } = useNuxtApp();

  let DMServiceInstance; // 一对一聊天服务

  // 这里的代码仅在客户端执行
  // 配置登录事件
  onMounted(async ()=>{
    console.log("1");
    if(localStorage.getItem("token")){
      // 确保所有子组件都渲染完毕
      console.log("2");
      
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
  
  $on("online", ()=>{
    DMServiceInstance = DMService.getInstance();
  });

  // 监听socket消息
  onMounted(()=>{
  });

  // 组件销毁时移除监听
  onBeforeUnmount(() => {
  });
</script>