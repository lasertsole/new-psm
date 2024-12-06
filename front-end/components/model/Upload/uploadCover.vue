<template>
  <transition name="fade">
    <div class="root" v-show="imageUrl" title="目标封面">
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
    </div>
  </transition>
</template>
  
<script lang="ts" setup>
  import { ref } from 'vue';

  const imageUrl:Ref<string | undefined> = ref<string | undefined>();
  function changeCover(blob:Blob) {

    imageUrl.value = URL.createObjectURL(blob);
  };

  defineExpose({
    changeCover
  });
</script>
  
<style lang="scss" scoped>
  .root{
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }
  }
</style>