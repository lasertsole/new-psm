<template>
    <div class="commend">
        <div class="top">
            <div class="left">
                <div class="profile" ></div>
                <div class="name">{{ name }}</div>
            </div>
            <div class="right">
                <!-- <div class="score">
                    <el-rate
                        v-model="starScore"
                        disabled
                    />
                </div> -->
            </div>
        </div>
        <div class="bottom">
            <div class="top">
                <div class="text">
                    {{ content }}
                </div>
            </div>
            <div class="bottom">
                <div class="date">
                    {{ createTime }}
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
    import { ref, defineProps, type PropType } from "vue";
    import { TargetTypeEnum } from "@/enums/review";

    const {ID, srcUserId, avatar, name, targetType, targetId, timestamp, content, createTime, likeNum, dislikeNum} = defineProps({
        ID:{
            type:String,
            required:true,
            validator: function (value:string):boolean {
                try {
                    return parseInt(value)>=0;
                } catch (error) {
                    return false;
                };
            }
        },
        srcUserId:{
            type:String,
            required:true,
            validator: function (value:string):boolean {
                try {
                    return parseInt(value)>=0;
                } catch (error) {
                    return false;
                };
            }
        },
        avatar:{type:String, required:true},
        name:{type:Number, required:true},
        targetType:{type:Object as PropType<TargetTypeEnum>, required:true},
        targetId:{
            type:String,
            required:true,
            validator: function (value:string):boolean {
                try {
                    return parseInt(value)>=0;
                } catch (error) {
                    return false;
                };
            }
        },
        timestamp:{type:String, required:true},
        content:{type:String, required:true},
        createTime:{type:String, required:true},
        likeNum:{
            type:Number,
            required:false, default: 0,
            validator: function (value:number):boolean {
                return value >= 0;
            }
        },
        dislikeNum:{
            type:Number,
            required:false,
            default: 0,
            validator: function (value:number):boolean {
                return value >= 0;
            }
        },
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;
    
    .commend{
        display: flex;
        flex-direction: column;
        >.top{
            @include common.fixedHeight(50px);
            display: flex;
            justify-content: space-between;
            align-items: center;
            >.left{
                display: flex;
                align-items: center;
                >.profile{
                    @include common.fixedCircle(50px);
                    background-image:url("images/MOYE.jpg");
                    background-size: 100%;
                }
                >.name{
                    margin-left: 7px;
                    font-weight: bolder;
                    font-size: 18px;
                }
            }
            >.right{
                >.score{
                }
            }
        }
        >.bottom{
            margin-top:10px;
            color:#707070;
            padding-bottom:2px;
            >.top{
                >.text{
                    font-weight: bold;
                    font-size: 14px;
                }
            }
            >.bottom{
                margin-top: 5px;
                >.date{
                    display: flex;
                    font-size: 12px;
                }
            }
        }
    }
</style>