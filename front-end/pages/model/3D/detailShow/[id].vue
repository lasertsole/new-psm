<template>
  <div class="showcaseDetail"
      ref="rootDom"
  >
    <div class="page">
      <div class="userBar">
        <div class="info" v-if="authorInfo">
          <img :src="authorInfo.avatar" class="avatar"/>
          
          <div class="text">
            <div class="name">{{authorInfo.name}}</div>
            <div class="profile">{{authorInfo.profile}}</div>
          </div>
          
        </div>
        
        <div class="action">
            <div class="follow">关注</div>
            <div class="sms">私信</div>
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
  import type { ModelInfoDetail } from "@/types/model";

  // 获取当前路由对象
  const route = useRoute();

  // 从 query 参数中获取 id
  const id = route.params.id;
  
  const modelInfoDetail = ref<ModelInfoDetail>();

  const authorInfo = computed(()=>{return modelInfoDetail.value?.user;});

  const modelInfo = computed(()=>{return modelInfoDetail.value?.model;});

  onMounted(async ()=>{
    let res : ModelInfoDetail = await getModelByModelId({modelId: id as string});
    if(res){
      modelInfoDetail.value = res;
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
      margin: 60px 4px 80px;

      >div{
        background-color: white;
        border-radius: 5px;
        overflow: hidden;
      }
      
      .userBar{
        @include fullWidth();
        @include fixedHeight(100px);
        padding: 20px 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .info{
          display: flex;

          .avatar{
            @include fixedCircle(60px);
            margin-right: 20px;
          }

          .text{
            display: flex;
            flex-direction: column;
            justify-content: center;
            font-size: 16px;

            .name{
              color: #707070;
            }

            .profile{
              color: #A6A6A6;
              margin-top: 5px;
            }
          }
        }

        .action{
          @include fixedRetangle(120px, 40px);
          
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          align-items: center;
          color: white;
          margin-top: 2px;
          >div{
              padding: 2px 10px;
              border-radius: 4px;
              cursor: pointer;
          }
          .follow{
              background-color: #fb7299;
          }
          .sms{
              background-color: #00a8e9;
          }
        }
      }
      
      .main{
        @include fullWidth();
        @include fixedHeight(550px);
        margin-top: 20px;
      }

      @media screen and (max-width: 800px) and (min-width: 600px) {
        @include fixedWidth(85%);

        margin-top: 40px;
        margin-bottom: 60px;
        .main{
          margin-top: 10px;
        }
      }
      
      @media screen and (max-width: 600px) {
        @include fullWidth;

        margin: 0px;
        .main{
          margin-top: 0px;
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