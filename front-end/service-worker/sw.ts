import { cacheNames, clientsClaim } from 'workbox-core';
import { precacheAndRoute } from 'workbox-precaching';

import {
  registerRoute,
  setCatchHandler,
  setDefaultHandler,
} from 'workbox-routing';
import type { StrategyHandler } from 'workbox-strategies';
import { NetworkFirst, NetworkOnly, CacheFirst, CacheOnly, Strategy } from 'workbox-strategies';
import type { ManifestEntry } from 'workbox-build';

declare let self: ServiceWorkerGlobalScope;
declare type ExtendableEvent = any;

// self.__WB_MANIFEST is default injection point
precacheAndRoute(self.__WB_MANIFEST);

const data = {
    race: false,
    debug: false,
    credentials: 'same-origin',
    networkTimeoutSeconds: 0,
    fallback: 'index.html',
};

const cacheName = cacheNames.runtime;

// manifest.json
const manifest = self.__WB_MANIFEST as Array<ManifestEntry>;
// 缓存的请求列表
const cacheEntries: RequestInfo[] = [];

const manifestURLs = manifest.map((entry) => {
    const url = new URL(entry.url, self.location);
    cacheEntries.push(
      new Request(url.href, {
        credentials: data.credentials as any,
      })
    );
    return url.href;
});

function buildStrategy(): Strategy {
  if (data.race) {
    class CacheNetworkRace extends Strategy {
      _handle(
        request: Request,
        handler: StrategyHandler
      ): Promise<Response | undefined> {
        const fetchAndCachePutDone: Promise<Response> =
          handler.fetchAndCachePut(request);
        const cacheMatchDone: Promise<Response | undefined> =
          handler.cacheMatch(request);

        return new Promise((resolve, reject) => {
          fetchAndCachePutDone.then(resolve).catch((e) => {
            if (data.debug) console.log(`Cannot fetch resource: ${request.url}`, e);
          });
          cacheMatchDone.then((response) => response && resolve(response));

          // Reject if both network and cache error or find no response.
          Promise.allSettled([fetchAndCachePutDone, cacheMatchDone]).then(
            (results) => {
              const [fetchAndCachePutResult, cacheMatchResult] = results;
              if (
                fetchAndCachePutResult.status === 'rejected' &&
                !cacheMatchResult.value
              )
                reject(fetchAndCachePutResult.reason);
            }
          );
        });
      }
    }
    return new CacheNetworkRace();
  } else {
    if (data.networkTimeoutSeconds > 0)
      return new NetworkFirst({ cacheName, networkTimeoutSeconds: data.networkTimeoutSeconds });
    else return new NetworkFirst({ cacheName });
  }
}

// 主要先存储一些文件
self.addEventListener('install', async(event)=>{
    caches.open(cacheName).then((cache)=>{
        return  cache.addAll(cacheEntries)
    });

    
    await self.skipWaiting();
});

self.addEventListener('activate', (event: ExtendableEvent) => {
  const debug = true; // 根据需要设置调试标志

  // 清理过时的运行时缓存
  event.waitUntil(
    caches.open(cacheName).then((cache) => {
      // 清理不在 manifestURLs 中的缓存项
      cache.keys().then((keys) => {
        const deletePromises = keys.map((request) => {
          if (debug) {
            console.log(`Checking cache entry to be removed: ${request.url}`);
          }
          if (!manifestURLs.includes(request.url)) {
            return cache.delete(request).then((deleted) => {
              if (debug) {
                if (deleted) {
                  console.log(`Precached data removed: ${request.url || request}`);
                } else {
                  console.log(`No precache found: ${request.url || request}`);
                }
              }
            });
          }
          return Promise.resolve(); // 如果不需要删除，返回一个已解析的 Promise
        });

        // 并行处理所有删除操作
        return Promise.all(deletePromises);
      });
    })
  );

  // 声明当前服务工作线程
  self.clients.claim();
});

registerRoute(({ url }) => manifestURLs.includes(url.href), new CacheFirst());

setDefaultHandler(buildStrategy());

// fallback to app-shell for document request
setCatchHandler(({ event }): Promise<Response> => {
  switch (event.request.destination) {
    case 'document':
      return caches.match(fallback).then((r) => {
        return r ? Promise.resolve(r) : Promise.resolve(Response.error());
      });
    default:
      return Promise.resolve(Response.error());
  }
});

// this is necessary, since the new service worker will keep on skipWaiting state
// and then, caches will not be cleared since it is not activated
self.skipWaiting();
clientsClaim();