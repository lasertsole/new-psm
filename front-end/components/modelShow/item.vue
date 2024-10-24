<template>
    <div class="itemBox">
        <div class="author_info">
            <div class="base">
                <div class="profile">
                    <img :src="`${boxInfo.user.avatar}`">
                </div>
                <div class="honour">
                    <div class="name">{{boxInfo.user.name}}</div>
                    <div class="publicModelNum">{{boxInfo.models.length}} 个公开模型</div>
                </div>
            </div>
            <div class="recomment">
                <div class="left">简介:</div>
                <div class="right">{{boxInfo.user.profile}}</div>
            </div>
            <div class="interesting">
                <div class="following">关注</div>
                <div class="directMessage">私信</div>
            </div>
        </div>
        <div class="author_works">
            <template v-for="(item, index) in boxInfo.models">
                <modelShowWorkBox
                    :ID="item.id"
                    :title="item.title"
                    :content="item.content"
                    :cover="item.cover"
                    :entity="item.entity"
                    :category="item.category"
                >
                </modelShowWorkBox>
            </template>
        </div>
    </div>
</template>

<script setup lang="ts">
    const props = defineProps({
        boxInfo:{type:Object as PropType<any>, required: true}
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    
    .itemBox{
        margin-top: 15px;
        height: 160px;
        border-bottom: 1px solid rgba(165, 165, 165, 0.3568627451);
        display: flex;
        flex-direction: row;
        .author_info{
            min-width: 150px;
            max-width: 150px;
            display: flex;
            flex-direction: column;
            overflow: hidden;
            .base{
                height: 50px;
                display: flex;
                flex-direction: row;
                .profile{
                    @include fixedSquare(50px);
                    border-radius: 50%;
                    overflow: hidden;
                    cursor: pointer;
                    img{
                        width: 100%;
                        height: 100%;
                    }
                }
                .honour{
                    margin-left: 10px;
                    overflow: hidden; /* 溢出部分隐藏 */
                    >*{
                        text-overflow: ellipsis; /* 文本溢出时显示省略号来代表被修剪的文本 */
                        overflow: hidden; /* 溢出部分隐藏 */
                        white-space: nowrap; /* 段落中的文本不进行换行 */
                    }
                    .name{
                        font-family: SourceSans3-Medium;
                        font-size: 15px;
                        font-weight: bold;
                    }
                    .publicModelNum{
                        color: #707070;
                        font-size: 10px;
                        line-height: 15px;
                    }
                }
            }
            .recomment{
                margin-top: 10px;
                font-size: 14px;
                color: #707070;
                display: flex;
                height: 65px;
                .left{
                    min-width: 35px;
                }
                .right{
                    overflow: hidden; /* 溢出部分隐藏 */
                    >*{
                        text-overflow: ellipsis; /* 文本溢出时显示省略号来代表被修剪的文本 */
                        overflow: hidden; /* 溢出部分隐藏 */
                        word-break: break-all;
                        table-layout: fixed; word-break:break-all;
                        word-wrap:break-word;
                    }
                }
            }
            .interesting{
                display: flex;
                flex-direction: row;
                justify-content: space-around;
                color: white;
                margin-top: 2px;
                >div{
                    padding: 2px 10px;
                    border-radius: 4px;
                    cursor: pointer;
                }
                .following{
                    background-color: #fb7299;
                }
                .directMessage{
                    background-color: #00a8e9;
                }
            }
        }
        .author_works{
            width: 100%;
            display: flex;
            flex-direction: row;
            justify-content: flex-start;
            padding-left: 15px;
            align-items: center;
        }
    }
</style>