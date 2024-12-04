export default defineNuxtPlugin(async (nuxtApp) => {
    const WujieVue = await import('wujie-vue3');
    nuxtApp.vueApp.use(WujieVue.default);
});