import localforage from 'localforage';

export default defineNuxtPlugin(() => {
    return {
      provide: {
        localforage
      }
    };
});