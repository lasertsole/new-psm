<template>
    <div class="itemBox">
        <div class="author_info">
            <div class="base">
                <div class="profile">
                    <CommonAvatar
                        :src="boxInfo.user.avatar"
                    >
                    </CommonAvatar>
                </div>
                <div class="honour">
                    <div class="name">{{boxInfo.user.name}}</div>
                    <div class="publicModelNum">{{boxInfo.user.publicModelNum!}} 个公开模型</div>
                    <div class="statusBar">
                        <div :class="{green: isIdle}">{{ isIdle?"空闲":"忙碌" }}</div>
                        <div :class="{green: canUrgent}">{{ canUrgent?"可加急":"不可加急" }}</div>
                    </div>
                </div>
            </div>
            <div class="recomment">
                <div class="left">简介:</div>
                <div class="right">{{boxInfo.user.profile}}</div>
            </div>
        </div>
        <div class="author_works">
            <template v-for="(item, index) in boxInfo.models" :key="item.id">
                <ModelShowWorkBox
                    :ID="item.id||''"
                    :title="item.title||''"
                    :style="item.style||''"
                    :type="item.type||''"
                    :createTime="item.createTime||''"
                    :optionStyle="style"
                    :optionType="type"
                >
                    <template #cover>
                        <CommonImage :src="typeof(item.cover) === 'string' ? item.cover : ''"></CommonImage>
                    </template>
                </ModelShowWorkBox>
            </template>
        </div>
    </div>
</template>

<script setup lang="ts">
    import type { Model3DInfos } from "~/types/model3d";
    import type { PropType } from "vue";
    
    const props = defineProps({
        boxInfo: {type:Object as PropType<Model3DInfos>, required: true},
        style: {type: String, required: false, default: ""},
        type: {type: String, required: false, default: ""},
        isIdle: {type: Boolean, required: false, default: true},
        canUrgent: {type: Boolean, required: false, default: true},
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;
    
    .itemBox{
        margin-top: 15px;
        padding-bottom: 15px;
        @include common.fixedHeight(235px);
        border-bottom: 1px solid rgba(165, 165, 165, 0.3568627451);
        display: flex;
        flex-direction: row;
        
        .author_info{
            @include common.fixedWidth(150px);
            display: flex;
            flex-direction: column;
            overflow: hidden;
            
            .base{
                height: 70px;
                display: flex;
                flex-direction: row;
                align-items: center;
                
                .profile{
                    @include common.fixedSquare(60px);
                    border-radius: 50%;
                    overflow: hidden;
                    cursor: pointer;
                    
                    img{
                        width: 100%;
                        height: 100%;
                    }
                }

                .honour{
                    flex: 1;
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

                    .statusBar{
                        color: #707070;
                        font-size: 12px;
                        display: flex;
                        flex-direction: column;
                        align-items: flex-start;

                        >div{
                            &::before{
                                @include common.fixedCircle(10px);
                                content: "";
                                background-color: orange;
                                margin-right: 5px;
                            }

                            &.green::before{
                                background-color: greenyellow;
                            }
                        }
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
        }
        
        .author_works{
            @include common.fixedHeight(220px);
            width: 100%;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); // 自动适应列数，每列最小宽度为 100px
            gap: 15px; // 网格项之间的间距
            justify-content: start;
            justify-items: start;
            @include common.scrollBar(8px);
            
            @media (min-width: 1020px){
                grid-template-columns: repeat(3, minmax(250px, 1fr));
            }
        }
    }
</style>