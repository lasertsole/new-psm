<template>
    <div class="home">
        <div class="container">
            <div class="personAbtract">
                <div class="avatar">
                    <CommonAvatar
                        :src="userInfo.avatar"
                    >
                    </CommonAvatar>
                </div>
                <el-main class="recomment"
                    v-loading="loading"
                >
                    <div class="recomment-name">{{userInfo.name}}</div>
                    <div class="recomment-status">
                        <el-switch
                            v-model="isIdle"
                            class="ml-2"
                            inline-prompt
                            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                            active-text="空闲"
                            inactive-text="忙碌"
                            :disabled="statusdisabled"
                        />
                        <el-switch
                            v-model="canUrgent"
                            class="ml-2"
                            inline-prompt
                            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                            active-text="可加急"
                            inactive-text="不可加急"
                            :disabled="statusdisabled"
                        />
                        <button @click="editstatusFunc">编辑信息</button>
                    </div>
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
                </el-main>
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
        </div>
    </div>
</template>

<script lang="ts" setup>
    const loading:Ref<boolean> = ref(false);

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

        loading.value=true;
        let ok = await updateAccountInfo({profile : tempProfile.value}).then((res)=>{
            if(res){tempProfile.value=userInfo.profile||"";}
        }).finally(()=>{loading.value=false});
    }, 1000);

    const isIdle = ref<boolean>(userInfo.isIdle!);
    const canUrgent = ref<boolean>(userInfo.canUrgent!);

    const isIdleRecord = ref<boolean>(userInfo.isIdle!);
    const canUrgentRecord = ref<boolean>(userInfo.canUrgent!);
    const statusdisabled = ref<boolean>(true);

    const editstatusFunc = debounce(async()=>{
        statusdisabled.value = !statusdisabled.value;
        if(statusdisabled.value&&(isIdle.value != isIdleRecord.value||canUrgent.value != canUrgentRecord.value)) {
            await updateAccountInfo({isIdle:isIdle.value, canUrgent:canUrgent.value}).finally(()=>{
                isIdleRecord.value = isIdle.value;
                canUrgentRecord.value = canUrgent.value;
            });
        };
    }, 1000);
    
    definePageMeta({
        name: 'home'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;

    .home{
        @include common.fullInParent;
        @include common.scrollBar(8px);
        padding: 30px 20px 30px 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
        
        .container{
            @include common.fullInParent;
            background-color: white;
            padding:30px;
            border-radius: 8px;
            overflow: hidden;
            
            .personAbtract{
                @include common.fullWidth;
                display: flex;
                flex-direction: column;
                align-items: center;
                padding-bottom: 15px;
                border-bottom: 1px solid rgba(165, 165, 165, 0.3568627451);
                
                .avatar{
                    @include common.fixedCircle(100px);
                    
                    margin-bottom: 5px;
                    img{
                        @include common.fullInParent;
                        object-fit: cover;
                    }
                }
                
                .recomment{
                    @include common.fixedWidth(80%);
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    font-size: 12px;
                    color: gray;
                    font-weight: bold;
                    
                    >*{
                        margin-bottom: 5px;
                    }

                    &-status{
                        color: #707070;
                        font-size: 12px;
                        display: flex;
                        align-items: center;

                        >*{
                            &:not(:first-child){
                                margin-left: 10px;
                            };
                        }

                        :deep(.el-switch__core){
                            width: 80px;
                        }

                        >button{
                            font-size: 12px;
                            font-weight: bold;
                            background-color: #00a8e9;
                            color: white;
                            padding: 2px 5px;
                            border-radius: 5px;
                            cursor: pointer;
                            transition: all .3s ease-in-out;
                            &:hover{
                                background-color: rgb(121.3,187.1,255);
                            };
                        }
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
                        @include common.fixedWidth(250px);
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
                @include common.fixedSquare(20px);
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
                    transition: all .3s ease-in-out;
                    &:hover{
                        background-color: rgb(121.3,187.1,255);
                    };
                }
            }   
        }
    }
</style>