<template>
    <div class="message">
        <el-main class="container">
            <div class="titleBox">
                <span>我的消息</span>
            </div>

            <div class="chatBox">
                <div class="left">
                    <div class="title">近期消息</div>
                    <div class="contactList"></div>
                </div>
                <div class="right">
                    <div class="title"></div>
                    <div class="messageList"></div>
                    <div class="sendBox"></div>
                </div>
            </div>
        </el-main>
    </div>
</template>

<script lang="ts" setup>

</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";

    .message {
        @include fullInParent();
        background-color: rgba(222, 222, 222, .75);
        display: flex;
        justify-content: center;
        overflow: hidden;

        .container{
            max-width: 980px;
            width: 80%;
            padding: 10px;
            display: flex;
            flex-direction: column;

            @include fixedWidth(80%);
            @include fixedHeight(100%);
            
            @media screen and (max-width: 800px) {
                @include fixedWidth(100%);
            }

            @media screen and (max-width: 700px) {
                padding: 10px 0px;
            }

            >div{
                box-shadow: 0 2px 4px 0 rgba(121, 146, 185, 0.54);
                border-radius: 4px;
                overflow: hidden;
            }

            $titleBoxHeight: 42px;
            .titleBox{
                @include fullWidth();
                @include fixedHeight($titleBoxHeight);
                font-size: 15px;
                color: #666;
                padding: 0px 16px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #f4f5f7;
            }

            $chatBoxMarginTop: 10px;
            .chatBox{
                @include fullWidth();
                margin-top: $chatBoxMarginTop;
                height: calc(100% - $titleBoxHeight - $chatBoxMarginTop);
                display: flex;

                >div{
                    @include fullHeight();

                    >div{
                        background-color: #f4f5f7;
                        overflow: hidden;
                    }

                    >.title{
                        @include fixedHeight(36px);
                        margin-bottom: 1.6px;
                    }
                }

                .left{
                    width: 240px;
                    margin-right: 1.6px;
                    display: flex;
                    flex-direction: column;

                    .title{
                        color: #666666;
                        font-size: 12px;
                        padding-left: 24px;
                        display: flex;
                        align-items: center;
                    }

                    .contactList{
                        flex-grow: 1;
                    }
                }
                
                .right{
                    flex-grow: 1;
                    display: flex;
                    flex-direction: column;

                    .title{
                        @include flexCenter;
                        color: #333333;
                        font-size: 14px;
                    }

                    .messageList{
                        flex-grow: 1;
                        margin-bottom: 0.8px;
                        overflow-y: scroll;

                        @include scrollBar(8px);
                    }

                    .sendBox{
                        height: 162px;
                        margin-top: 0.8px;
                    }
                }
            }
        }
    }
</style>