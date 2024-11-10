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
  const { $emit, $socket } = useNuxtApp();

  // 这里的代码仅在客户端执行
  // 配置登录事件
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

  // 监听socket消息
  onMounted(() => {
    setInterval(()=>{
      $socket.emit('chat message', 'hello')
    }, 500);
    $socket.on('chat message', (msg) => {
      console.log('Received message:', msg)
    })
  })

  // 组件销毁时移除监听
  onBeforeUnmount(() => {
    $socket.off('chat message')
  })
</script>