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
  *{
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    object-fit: contain;
  }
  a{
    text-decoration: none;
    color: inherit;
  }
  ul{
    list-style: none;
  }
</style>

<script lang="ts" setup>
  const { $on }= useNuxtApp();
  import {onMounted} from "vue"
  // 这里的代码仅在客户端执行
  onMounted(()=>{
    localStorage.setItem("online", "false");
  })
</script>