export default defineNuxtConfig({
    runtimeConfig: {
        // apiSecret 只能在服务器端上访问
        apiSecret: '123',
        // public 命名空间中定义的，在服务器端和客户端都可以普遍访问
        public: {
            apiBase: process.env.NUXT_PUBLIC_API_BASE
        }
    }
})
