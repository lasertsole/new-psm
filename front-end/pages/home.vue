<template>
    <div class="personSpace">
        <div class="personAbtract">
            <div class="avatar">
                <img :src="userInfo.avatar">
            </div>
            <div class="recomment">
                <div class="recomment-name">{{userInfo.name}}</div>
                <div class="recomment-title">个人简介:</div>
                <el-input
                    class="recomment-content"
                    v-model="tempProfile"
                    :autosize="{ minRows: 3 }"
                    type="textarea"
                    size="large"
                    placeholder="暂无简介"
                    :disabled="!profileEditable"
                    maxlength="255"
                    show-word-limit
                    resize="none"
                    style="border-radius:5px; overflow: hidden;"
                />
                <el-button type="primary" @click="editProfileFunc">编辑信息</el-button>
            </div>
        </div>
        <div class="showcase">
            <h2>
                <img class="icon" src="/icons/Vector.png" alt="">
                <span>橱窗</span>
            </h2>
            <div class="more">
                <div class="moreButton">查看更多</div>
            </div>
            <div class="boxContainer">
                <!-- <adaptContainer
                    :boxNum="showCaseBoxArr.length"
                    :boxWidth="290"
                    :boxHeight="180"
                >
                    <template v-for="(item, index) in showCaseBoxArr" #[index+1]>
                        <showCaseBox
                            :imgSrc="item.imgSrc"
                            :describt="item.describt"
                            ref="showCaseBoxDom"
                        ></showCaseBox>
                    </template>
                </adaptContainer> -->
            </div>
        </div>
        <div class="planning">
            <h2>
                <img class="icon" src="/icons/planning.png" alt="">
                <span>企划</span>
            </h2>
            <div class="more">
                <div class="moreButton">查看更多</div>
            </div>
            <div class="boxContainer">
                <!-- <adaptContainer
                    :boxNum="planningBoxArr.length"
                    :boxWidth="500"
                    :boxHeight="180"
                >
                    <template v-for="(item, index) in planningBoxArr" #[index+1]>
                        <itemBox
                            :title="item.title"
                            :describe="item.describe"
                            :imgSrc="item.imgSrc"
                            :type="item.type"
                            :calendar="item.calendar"
                            :price="item.price"
                        ></itemBox>
                    </template>
                </adaptContainer> -->
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
    // import itemBox from "components/planning/itemBox.vue";
    // import adaptContainer from "components/common/adaptContainer.vue"
    // import showCaseBox from "components/personSpace/showCaseBox.vue"
    // import { ref } from "vue"
    // import { showCaseBoxInfo, planningBoxInfo} from "types/pageType/personSpace"

    const profileEditable = ref<boolean>(false);// 是否可以编辑个人简介

    const tempProfile = ref<string>(userInfo.profile||"");
    
    const editProfileFunc = debounce(async()=>{
        profileEditable.value=!profileEditable.value;
        
        if(profileEditable.value){
           return; 
        }

        if(tempProfile.value==userInfo.profile){
            ElMessage.error("现简介不能和原简介相同");  
            return;
        }

        let ok = await updateAccountInfo({profile : tempProfile.value});
        if(ok){
            tempProfile.value=userInfo.profile||"";
        };
    });

    // /*橱窗盒子的数据*/
    // const showCaseBoxArr = ref<showCaseBoxInfo[]>([
    //     {
    //         imgSrc: "Carousel/bg-1.jpg",
    //         describt: "虚拟主播 视频封面（16:10）"
    //     },
    //     {
    //         imgSrc: "Carousel/bg-1.jpg",
    //         describt: "虚拟主播 视频封面（16:10）"
    //     },
    //     {
    //         imgSrc: "Carousel/bg-1.jpg",
    //         describt: "虚拟主播 视频封面（16:10）"
    //     }
    // ])
    //  /*企划盒子的数据*/
    //  const planningBoxArr = ref<planningBoxInfo[]>([
    //     {
    //         title:"双人联动轴",
    //         describe:"双人轴 带点日语和英语 需要和谐部分词汇",
    //         imgSrc:"Carousel/bg-1.jpg",
    //         type:"美工",
    //         calendar:"2023-5-10",
    //         price:"￥150-250",
    //     },
    //     {
    //         title:"双人联动轴",
    //         describe:"双人轴 带点日语和英语 需要和谐部分词汇",
    //         imgSrc:"Carousel/bg-1.jpg",
    //         type:"美工",
    //         calendar:"2023-5-10",
    //         price:"￥150-250",
    //     },
    //     {
    //         title:"双人联动轴",
    //         describe:"双人轴 带点日语和英语 需要和谐部分词汇",
    //         imgSrc:"Carousel/bg-1.jpg",
    //         type:"美工",
    //         calendar:"2023-5-10",
    //         price:"￥150-250",
    //     }
    // ])
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    @mixin fixedCircle($size){
        @include fixedSquare($size);
        overflow: hidden;
        border-radius: 50%;
    }

    .personSpace{
        width: 100%;
        height: 100%;
        padding: 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .personAbtract{
            width: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-bottom: 15px;
            border-bottom: 1px solid rgba(165, 165, 165, 0.3568627451);
            @include fullWidth;
            
            .avatar{
                @include fixedCircle(100px);
                margin-bottom: 5px;
                img{
                    @include fullInParent;
                    object-fit: cover;
                }
            }
            .recomment{
                display: flex;
                flex-direction: column;
                align-items: center;
                font-size: 12px;
                color: gray;
                font-weight: bold;
                @include fixedWidth(80%);
                
                >*{
                    margin-bottom: 5px;
                }

                &-name{
                    font-size: 18px;
                    color: black;
                }

                &-title{
                    margin: 10px 0px;
                    font-size: medium;
                }

                &-content{
                    resize: none;
                    border: 1px solid #00a8e9;
                }
            }
        }
        .showcase{
            width: 100%;
            border-bottom: 1px solid rgba(165, 165, 165, 0.3568627451);
            padding-bottom: 15px;
            h2{
                padding: 13px 0px;
                text-align: center;
            }
            .boxContainer{
                display: flex;
                flex-direction: row;
                justify-content: center;
                .box{
                    @include fixedWidth(250px);
                    margin: 0px 20px;
                    .top{
                        overflow: hidden;
                        border-radius: 12px;
                        height: 150px;
                        img{
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                        }
                    }
                    .bottom{
                        padding: 10px 0px 0px 10px;
                        height: 20px;
                        font-size: 12px;
                        font-weight: bolder;
                    }
                }
            }
        }
        .planning{
            width: 100%;
            border-bottom: 1px solid rgba(165, 165, 165, 0.3568627451);
            padding-bottom: 15px;
            h2{
                padding: 13px 0px;
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .boxContainer{
                width: 100%;
                display: flex;
                flex-direction: row;
                justify-content: flex-start;
                align-content: flex-start;
                align-items: flex-start;
                flex-wrap: wrap;
                overflow-y: auto;
            }
        }

        .icon{
            @include fixedSquare(20px);
            margin-right: 10px;
        }
        .more{
            display: flex;
            justify-content: flex-end;
            .moreButton{
                font-size: 12px;
                font-weight: bold;
                background-color: #00a8e9;
                color: white;
                padding: 2px 5px;
                border-radius: 5px;
                cursor: pointer;
            }
        }
    }
</style>