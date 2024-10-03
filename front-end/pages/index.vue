<template>
   <div class="indexPage">
        <div class="top">
            <div class="info">
               <div class="title">定制需求，成品交易，一举两得</div>
           </div>
        </div>
        <div class="bottom">
            <!-- <IndexPageCarousel
                :carouselProcessArr="carouselProcessArr"
             >
            </IndexPageCarousel> -->
        </div>
   </div>
</template>

<script lang="ts" setup>
   const carouselProcessArr:string[] = ['/carousel/bg-1.jpg', '/carousel/bg-2.jpg', '/carousel/bg-3.jpg'];
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .indexPage{
       @include fullInParent;
       position: relative;
       z-index: 1;
       .top{
           @include fullInParent;
           position: absolute;
           z-index: 1;
           background-color: rgba($color: #000000, $alpha: .2);
           display: flex;
           flex-direction: column;
           justify-content: center;
           align-items: center;
           .info{
               max-width: 700px;
               height: 370px;
               display: flex;
               flex-direction: column;
               justify-content: center;
               align-items: center;
               .title{
                   font-size: 34px;
                   color: white;
                   font-weight: bold;
                   padding: 0px 20px;
               }
           }
       }
        .bottom{
           @include fullInParent;
           position: relative;
           z-index: 0;
        }
   }
</style>