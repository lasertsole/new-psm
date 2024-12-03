// plugins/v-counter.js
export default defineNuxtPlugin((nuxtApp) => {
    nuxtApp.vueApp.directive('clickOutside', {
        mounted(el, binding) {
            // 指向外部点击事件的处理函数
            function clickOutside(event: MouseEvent) {
                // 检查点击是否发生在el之外
                if (!(el === event.target || el.contains(event.target as Node))) {
                    // 如果是，调用绑定的方法
                    binding.value(event);
                }
            }
            // 添加事件监听器
            document.addEventListener('click', clickOutside);
            // 设置一个反向操作，防止内存泄漏
            el._clickOutside = clickOutside;
        },
        unmounted(el) {
            document.removeEventListener('click', el._clickOutside);
            delete el._clickOutside;
        }
    })
});
