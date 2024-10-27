<template>
  <div class="showcaseDetail"
      ref="rootDom"
  >
    <div class="page">
      <!-- 左栏 -->
      <div class="leftBar">
        <div class="runningVideo"
            ref="videoControllBoxDom"
        >
            <commonModel
              v-if="modelInfo && modelInfo.entity"
              :entity="(modelInfo.entity as string)"
            ></CommonModel>
        </div>

        <div class="tabBar" ref="tabBarDiv">
            <!-- <tabBar 
                :tabList="tabList"
                @changeClassifyIndex="changeClassifyIndex"
                :focusIndex="classifyIndex"
            ></tabBar> -->
            <div class="report">举报橱窗</div>
        </div>
        <div class="detailBox" ref="detailBox">
            <!-- <showcaseDetailInfo :info="params?.mainInfo&&JSON.parse(params.mainInfo).detail"></showcaseDetailInfo>
            <createPhase :info="params?.mainInfo&&JSON.parse(params.mainInfo).phrase"></createPhase>
            <commendOfShowcase :article="`2`"></commendOfShowcase> -->
        </div>
      </div>

      <!-- 右栏 -->
      <div class="rightBar">
        
      </div>
      
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { ModelInfo } from "@/types/model";

  // 获取当前路由对象
  const route = useRoute();

  // 从 query 参数中获取 id
  const id = route.query.id;
  
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
    width: 100%;
    min-height: 100%;
    background-color: white;
    $paddingCol: 30px;
    padding: $paddingCol 80px $paddingCol 80px;
    display: flex;
    flex-wrap: wrap;//使page填满整个容器的关键属性
    justify-content: center;
    box-sizing: border-box;
    .page{
        @include fixedWidth(1000px);
        padding: 20px;
        display: flex;
        justify-content: space-between;

        .leftBar{
          @include fixedRetangle(60%, 100%);
          
          .runningVideo{
              height: 300px;

          }
          
          .tabBar{
            background-color: white;
            margin-top: 20px;
            display:flex;
            flex-direction: column;
            position: sticky;
            z-index: 10;
            top: -$paddingCol - 1px;
            
            &::v-deep(.classify){
              position: relative;
              z-index: 1;
            }
            
            .report{
              position: absolute;
              z-index: 3;
              display: inline-block;
              right: 5px;
              bottom: 12px;
              font-size: 12px;
              color: #b3b3b3;
              font-weight: bold;
              cursor: pointer;
            }
          }
          
          .detailBox{
            @include fixedRetangle(100%, 1000px);
            display: flex;
            flex-direction: column;
            overflow: hidden;
            z-index: 5;
            position: sticky;
          }
      }

      .rightBar{
        @include fixedRetangle(35%, 100%);
      }
    }
  }
</style>