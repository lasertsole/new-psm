import mitt from 'mitt';
import { defineNuxtPlugin } from '#app'

export default defineNuxtPlugin(() => {
  const emitter = mitt();
  return {
    provide: {
      emit: emitter.emit,// 触发事件方法 $emit
      on: emitter.on// 监听事件方法 $on
    }
  }
})
