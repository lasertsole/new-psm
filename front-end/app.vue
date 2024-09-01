<script setup lang="ts">
</script>

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
    localStorage.setItem("online", "false");
  })
</script>