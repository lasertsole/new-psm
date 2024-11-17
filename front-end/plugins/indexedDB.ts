import { defineNuxtPlugin } from '#app';

export default defineNuxtPlugin((nuxtApp) => {
  if (import.meta.client) {
    // 客户端环境
    const dbPromise = openDatabase();

    nuxtApp.provide('indexedDB', {
      getDb: () => dbPromise,
    });
  }
});

function openDatabase() {
  return new Promise<IDBDatabase>((resolve, reject) => {
    const request = indexedDB.open('myDatabase', 1);

    request.onerror = (event) => {
      reject(event.target.error);
    };

    request.onsuccess = (event) => {
      resolve(event.target.result);
    };

    request.onupgradeneeded = (event) => {
      const db = event.target.result;
      // 创建对象存储库
      db.createObjectStore('myStore', { keyPath: 'id' });
    };
  });
}