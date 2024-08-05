import { resolve } from 'pathe'
// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: false },
  modules: [
    '@element-plus/nuxt'
  ],
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
  vite: {
    resolve: {
      alias: {
        '~': resolve(__dirname, './'),
        '@': resolve(__dirname, './'),
      },
    },
  },
  nitro: {
    devProxy: {
      "/api": {
        target: process.env.VITE_API_BASE_URL, // 这里是接口地址
        changeOrigin: true,
        prependPath: true,
      },
    }
  },
  runtimeConfig: {
    public: {
      baseURL: process.env.VITE_BASE_URL,
      apiBaseURL: "/api"
    }
  }
})

