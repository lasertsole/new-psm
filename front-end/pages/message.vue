<template>
    <el-row :gutter="20">
        <el-col :span="4" style="width: 100%;"></el-col>
        <el-col :span="20">
            <div class="common-layout">
                <el-container>
                    <el-aside width="120px">
                        <div style="display: flex; justify-content: center; font-size: 2em; margin-top: 24px;">
                            <el-icon><Position/></el-icon>
                            <el-text>消息中心</el-text>
                        </div>
                        <el-divider />
                        <el-button 
                            v-for="button in buttons"
                            :type="button.type"
                            :icon="button.icon"
                            @click="handleOpen(button.key)"
                            text
                            style="margin-left:0px"
                            >
                            {{ button.text }}
                        </el-button>
                        <el-divider />
                        <el-button 
                            :type="settingButtons.type"
                            :icon="settingButtons.icon"
                            @click="handleOpen(settingButtons.key)"
                            text
                            style="margin-left:0px"
                            >
                            {{ settingButtons.text }}
                        </el-button>
                    </el-aside>
                    <el-main>
                        <el-row style="height: 30px;">
                            <el-col :span="4">
                                <el-text>{{ textare.title }}</el-text>
                            </el-col>
                            <el-col :span="20">

                            </el-col>
                        </el-row>
                        <el-row style="height: 100%;">
                            <el-col :span="4">
                                <el-scrollbar height="100%">
                                    <el-button v-for="userone in textare.user" style="height: 50px; margin-left:0px">
                                        <el-row>
                                            <el-col :span="5">
                                                <el-avatar :src="userone.avatar"/>
                                            </el-col>
                                            <el-col :span="19">
                                                <el-row><el-text>{{ userone.uername }}</el-text></el-row>
                                                <el-row><el-text type="info" truncated>{{ userone.introduction }}</el-text></el-row>
                                            </el-col>
                                        </el-row>
                                    </el-button>
                                </el-scrollbar>
                            </el-col>
                            <el-col :span="20">

                            </el-col>
                        </el-row>
                    </el-main>
                </el-container>
            </div>
        </el-col>
        <el-col :span="42" style="width: 100%;"></el-col>
    </el-row>
</template>

<script lang="ts" setup>
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
const Position = ElementPlusIconsVue.Position
const buttons = ref([
  { type: 'primary', text: '我的消息', icon: ElementPlusIconsVue.Message, key: 'userMessage'},
  { type: '', text: '回复我的', icon: ElementPlusIconsVue.Opportunity, key: 'userReply'},
  { type: '', text: '@ 我的', icon: ElementPlusIconsVue.PhoneFilled, key: 'userAt'},
  { type: '', text: '收到的赞', icon: ElementPlusIconsVue.Star, key: 'userLike'},
  { type: '', text: '系统通知', icon: ElementPlusIconsVue.Notification, key: 'userSystem'},
]);
const settingButtons = ref({ type: '', text: '消息设置', icon: ElementPlusIconsVue.Setting, key: 'userSetting'})
const textare = ref({
     title: '近期消息', 
     user: [
        { avatar: 'https://i2.hdslb.com/bfs/face/2f2e35a321f07d5505f38b82ffa3dc6b165d43e6.jpg',uername: 'suiii', introduction: '你好，我是小明，欢迎来到小明的消息中心' },
        { avatar: 'https://i2.hdslb.com/bfs/face/07d95e2a5d27c738edbe830b61876eec15505780.jpg',uername: 'iru', introduction: '你好，我是小红，欢迎来到小红的消息中心' },
        { avatar: 'https://i2.hdslb.com/bfs/face/7187defa87afeef03f4f9e333b8bf4015373c7cd.jpg',uername: 'kuma', introduction: '你好，我是小刚，欢迎来到小刚的消息中心' },
     ], })  
const handleOpen = (key: string) => {
  buttons.value.forEach(button => {
    button.type = '';
  });
  settingButtons.value.type = '';
  const button = buttons.value.find(button => button.key === key)
  if (button) {
    button.type = 'primary'
  }else{
    settingButtons.value.type = 'primary'
  }
}
</script>

<style lang="scss" scoped>
</style>