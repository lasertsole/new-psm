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

// 缓存过期时间(单位毫秒)
const CACHE_TIMEOUT:number = 1000 * 60 * 60 * 24 * 1; // 1天

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

// 主要清除旧的缓存
self.addEventListener('activate', async(event: ExtendableEvent)=>{
    event.waitUntil(
        caches.open(cacheName).then((cache)=>{
            cache.keys().then(keys=>{
                keys.forEach(async (request)=>{
                    // 从缓存中获取响应
                    cache.match(request).then(async (tempRes)=>{
                        if(!tempRes) return;
                        const response:Response = tempRes;
                        
                        const tempDate = response.headers.get('Date');
                        if(!tempDate) return;
                        const timestampHeader:string = tempDate;
                        
                        const cacheTimestamp = new Date(timestampHeader);
                        const currentTime = new Date();
                        
                            // 计算时间差（毫秒）
                        const timeDifference = currentTime.getTime() - cacheTimestamp.getTime();
                        
                        //判断是否过期,如果超过缓存过期时间，则删除  (强制控制缓存时间)
                        if(CACHE_TIMEOUT<timeDifference){
                            await cache.delete(request);
                        }; 
                    });
                });
            });
        })
    );
    
    await self.clients.claim();
});

registerRoute(({ url }) => manifestURLs.includes(url.href), buildStrategy());

setDefaultHandler(new NetworkFirst());

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