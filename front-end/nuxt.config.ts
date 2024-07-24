import { resolve } from 'pathe'
// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: false },
  modules: [
    '@pinia/nuxt',
    '@pinia-plugin-persistedstate/nuxt',
    '@element-plus/nuxt'
  ],
  piniaPersistedstate: {
    storage: 'localStorage'
  },
  app:{
    head:{
      title: '喵字幕',
      meta: [
          { name: "description", content: "满足翻译商稿的买卖平台"},
          { name: "keyword", content: "翻译,买卖,接单" }
      ],
      link: [
        { rel: "icon", type: "image/x-icon", href: "tab_icon.png" }
      ]
    }
  },
})
