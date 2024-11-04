<template>
  <div class="detailShow"
      ref="rootDom"
  >
    <div class="page">
      <div class="userBar">
        <div class="info" v-if="authorInfo">
          <img :src="authorInfo.avatar||'/images/defaultAvatar.png'" class="avatar"/>
          
          <div class="text">
            <div class="name">{{authorInfo.name}}</div>
            <div class="profile">{{authorInfo.profile}}</div>
          </div>
          
        </div>
        
        <div class="action">
            <div class="follow" @click="triggerFollow()">{{isFollowing?'已关注':'关注'}}</div>
            <div class="sms">私信</div>
        </div>
      </div>
      
      <div class="main">
        <commonModel
          class="entity"
          v-if="modelInfo && modelInfo.entity"
          :entity="(modelInfo.entity as string)"
        ></CommonModel>

        <div class="info">
          <div class="createTime">
            上传时间: {{ modelInfo 
            && modelInfo.createTime 
            && new Date(modelInfo?.createTime).toLocaleDateString('zh-CN', {
              year: 'numeric',
              month: 'numeric',
              day: 'numeric'
            }) }}
          </div>

          <div class="category"
            v-if="modelInfo"
          >
            <span class="style">风格: {{ modelInfo.category&&styleEnumObject[modelInfo.category.style] }}</span>
            <span class="type">类型: {{ modelInfo.category&&typeEnumObject[modelInfo.category.type] }}</span>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { UserInfo } from "@/types/user";
  import { StyleEnum, TypeEnum } from "@/enums/models.d";
  import type { ModelInfoDetail, ModelInfo } from "@/types/model";

  // 将枚举转换为对象
  const styleEnumObject = Object.fromEntries(Object.entries(StyleEnum).map(([k, v]) => [v, k]));
  const typeEnumObject = Object.fromEntries(Object.entries(TypeEnum).map(([k, v]) => [v, k]));

  // 获取当前路由对象
  const route = useRoute();

  // 从 query 参数中获取 id
  const id = route.params.id;

  const modelInfoDetail = ref<ModelInfoDetail>();

  const authorInfo: ComputedRef<UserInfo | undefined> = computed(()=>{return modelInfoDetail.value?.user;});

  const modelInfo: ComputedRef<ModelInfo | undefined> = computed(()=>{return modelInfoDetail.value?.model;});

  const isFollowing: Ref<boolean> = ref<boolean>(false);

  const triggerFollow = debounce(async ():Promise<void>=> {
    if(!authorInfo.value) return;

    if(!isFollowing.value) {
      if(await followUser(authorInfo.value.id!)) isFollowing.value = !isFollowing.value;
    }
    else {
      if(await unFollowUser(authorInfo.value.id!)) isFollowing.value = !isFollowing.value;
    }
  }, 1000);

  onMounted(async ()=>{
    let res : ModelInfoDetail = await getModelByModelId({modelId: id as string});
    if(res){
      modelInfoDetail.value = res;
      isFollowing.value = res.user.isFollowed!;
    }
  });

  definePageMeta({
    name: 'model-3D-detailShow'
  });
</script>

<style lang="scss" scoped>
  @use "sass:math";
  @import "@/common.scss";

  .detailShow{
    @include fullWidth();
    min-height: 100%;
    display: flex;
    justify-content: center;
    background-color: rgba(222, 222, 222, .75);

    .page{
      @include fixedWidth(65%);
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
            @include fixedRetangle(52px, 26px);
            @include flexCenter();
            
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
        margin-top: 20px;

        .entity{
          @include fixedHeight(550px);
        }
        
        .info{
          padding: 20px;
          font-size: 12px;
          color: #9b9b9b;
          display: flex;
          justify-content: space-between;

          .createTime{
            margin-bottom: 10px;
          }
          
          .category{
            .style{
              margin-right: 10px;
            }
          }
        }
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
  }
</style>