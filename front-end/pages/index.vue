<template>
   <div>
      <NuxtLayout name="tool">
         <div class="text-red-400">工具页模板xxxxxx</div>
         <el-button type="primary" size="default" @click="buttonClick">测试存储按钮</el-button>
      </NuxtLayout>
   </div>
</template>

<script setup>
   import { useLocalStore } from '@/store/useLocalStore' 

   const localStore = useLocalStore()

   const buttonClick=()=>
   {
      console.log(localStore.name)
      console.log(localStore.age)
   }
   //浏览器执行
   if(process.client)
   {
      //localStore
      localStore.setName("localStore存储测试")
      localStore.setAge(18)
   }
   //前端服务器执行
   if(process.server)
   {
      console.log("服务器执行")
   }
   console.log("测试") //client,server都会执行
</script>

<style scoped lang="scss">
   .index{
      color:red;
      .div{
         color:blue;
      }
   }
</style>