import { resolve } from 'pathe'
// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  // 不使用开发工具
  devtools: { enabled: false },

  // 导入第三方模块
  modules: [
    '@element-plus/nuxt'
    ,'@vite-pwa/nuxt'
  ],
  
  // pwa: {
  //   strategies: "generateSW",
  //   srcDir: "service-worker",
  //   filename: "sw.ts",
  //   registerType: 'autoUpdate',
  //   manifest: {
  //     name: process.env.VITE_APP_NAME,
  //     short_name: process.env.VITE_APP_NAME,
  //     icons: [
  //       {
  //         src: '/favicon/tab_icon.png',
  //         sizes: '192x192',
  //         type: 'image/png',
  //       },
  //       {
  //         src: '/favicon/tab_icon.png',
  //         sizes: '512x512',
  //         type: 'image/png',
  //       },
  //     ],
  //     theme_color: '#ffffff',
  //     display: 'standalone',
  //     start_url: '/',
  //   },

  //   workbox:{
  //     runtimeCaching: [
  //       {
  //         urlPattern: new RegExp(process.env.VITE_API_FRONT_URL!.replace(/\//g, '\\/') + '\\/.*', 'i'),
  //         method: 'GET',
  //         handler: 'CacheFirst',
  //         options: {
  //           cacheName: 'frontEndCache',
  //           expiration: {
  //             maxAgeSeconds: 60 * 60 * 24 * 1, // <== 缓存过期时间1 days
  //           },
  //           cacheableResponse: {
  //             statuses: [0, 200], // 0表示离线访问
  //           },
  //         },
  //       }
  //       ,{
  //         urlPattern: new RegExp(process.env.VITE_API_BACK_URL!.replace(/\//g, '\\/') + '\\/.*', 'i'),
  //         handler: 'NetworkFirst',
  //         options: {
  //           cacheName: 'backEndCache',
  //           expiration: {
  //             maxAgeSeconds: 60 * 60* 24 * 0.5, // <== 缓存过期时间0.5 days
  //           }
  //         }
  //       }
  //     ]
  //   },
    
  //   devOptions: {
  //     enabled: true,
  //     suppressWarnings: true,
  //     navigateFallback: '/',
  //     navigateFallbackAllowlist: [/^\/$/],
  //     type: 'module',
  //   },
  // },

  //项目信息
  app:{
    head:{
      title: process.env.VITE_APP_NAME,
      // 方便搜索引擎查找
      meta: [
          { name: "description", content: "定制、成品买卖平台"},
          { name: "keyword", content: "建模,买卖,接单,定制,成品" }
      ],
      // tab图标
      link: [
        { rel: "icon", type: "image/x-icon", href: "/favicon/tab_icon.png" }
      ]
    }
  },
  // 全局css
  css:['@/main.scss'],

  vite: {
    resolve: {
      alias: {
        '~': resolve(__dirname, './'),
        '@': resolve(__dirname, './'),
      },
    },
  },

  // 路由策略
  routeRules: {
    '/index': { redirect: '/' },
    '/login': { redirect: '/loginOrRegister/login' },
    '/register': { redirect: '/loginOrRegister/register' },
    '/thirdLogin': { redirect: '/loginOrRegister/thirdLogin' },
    '/model/3D/show': { redirect: '/model/3D/show/all' },
  },

  // 代理
  nitro: {
    prerender:{ //预渲染
      routes: [
        '/' //首页(默认渲染)
        ,"/loginOrRegister/login" //登录页
        ,"/loginOrRegister/register" //注册页
        ,"/loginOrRegister/thirdLogin" //第三方登录回调页
      ]
    }
  },
  runtimeConfig: {
    public: {
      appName: process.env.VITE_APP_NAME
      ,appDomainName: process.env.VITE_APP_DOMAIN_NAME
      ,baseURL: process.env.VITE_API_BACK_URL
      ,apiBaseURL: "/api"
      ,oauth2AuthURL: process.env.VITE_OAuth2_Auth_URL//第三方登录授权地址
    }
  }
})