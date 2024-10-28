<template>
  <div class="showcaseDetail"
      ref="rootDom"
  >
    <div class="page">
      <div class="userBar">
        <div class="info">
          <img :src="userInfo.avatar" class="avatar"/>
        </div>
        
        <div class="action">
          
        </div>
      </div>
      
      <div class="main">
        <commonModel
          v-if="modelInfo && modelInfo.entity"
          :entity="(modelInfo.entity as string)"
        ></CommonModel>

        <div class="info">
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { ModelInfo } from "@/types/model";

  // 获取当前路由对象
  const route = useRoute();

  // 从 query 参数中获取 id
  const id = route.params.id;
  
  const modelInfo = ref<ModelInfo>();

  onMounted(async ()=>{
    let res : ModelInfo = await getModelByModelId({modelId: id as string});
    if(res){
      modelInfo.value = res;

      
    }
  });
  

  definePageMeta({
    name: 'model-3D-detailShow'
  });
</script>

<style lang="scss" scoped>
  @use "sass:math";
  @import "@/common.scss";

  .showcaseDetail{
    @include fullInParent();
    display: flex;
    justify-content: center;
    overflow: auto;
    background-color: rgba(222, 222, 222, .75);

    .page{
      @include fixedRetangle(65%, 100%);
      margin: 60px 4px 100px;

      >div{
        background-color: white;
        border-radius: 5px;
        overflow: hidden;
      }
      
      .userBar{
        @include fullWidth();
        @include fixedHeight(100px);
        padding: 20px 30px;

        .info{
          display: flex;
          justify-content: space-between;
          .avatar{
            @include fixedCircle(60px);
          }
        }

        .action{

        }
      }
      
      .main{
        @include fullWidth();
        @include fixedHeight(550px);
        margin-top: 20px;
        
        .info{
          
        }
      }
    }

    /* 滚动条整体部分 */
    &::-webkit-scrollbar {
      width: 10px; /* 滚动条的宽度 */
      height: 10px; /* 水平滚动条的高度 */
    }

    /* 滚动条的滑块部分 */
    &::-webkit-scrollbar-thumb {
      background-color: darkgrey; /* 滑块的颜色 */
      border-radius: 10px; /* 滑块的圆角 */
    }

    /* 滚动条的轨道部分 */
    &::-webkit-scrollbar-track {
      background-color: lightgrey; /* 轨道的颜色 */
      border-radius: 10px; /* 轨道的圆角 */
    }
  }
</style>