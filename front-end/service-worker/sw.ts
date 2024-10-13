declare let self: ServiceWorkerGlobalScope;

// sw.js
const CACHE_BASE_NAME:string = 'cache_v';
const CACHE_version:number  = 1;
const CACHE_NAME:string  = CACHE_BASE_NAME + CACHE_version;
const noCacheUrlArr:string[] = ['/fastLogin'] // 允许跳过的url列表

// 缓存过期时间(单位毫秒)
const CACHE_TIMEOUT:number = 1000 * 60 * 60 * 24 * 1; // 1天

// 主要先存储一些文件
self.addEventListener('install', async(event)=>{
    const cache = await caches.open(CACHE_NAME);
    await cache.addAll([
        '/',
    ])
    
    await self.skipWaiting();
});

// 主要清除旧的缓存
self.addEventListener('activate', async(event)=>{
    const cache = await caches.open(CACHE_NAME);
    const entries  = await cache.keys();
    for (const entry of entries) {
        // 从缓存中获取响应
        const tempRes = await cache.match(entry);
        if(!tempRes) continue;
        const response:Response = tempRes;
        
        const tempDate = response.headers.get('Date');
        if(!tempDate) continue;
        const timestampHeader:string = tempDate;
        
        const cacheTimestamp = new Date(timestampHeader);
        const currentTime = new Date();
        
         // 计算时间差（毫秒）
        const timeDifference = currentTime.getTime() - cacheTimestamp.getTime();
        
        //判断是否过期,如果超过缓存过期时间，则删除  (强制控制缓存时间)
        if(CACHE_TIMEOUT<timeDifference){
            await cache.delete(entry);
        };
    }
    
    await self.clients.claim();
});

// 拦截请求，可以从浏览器拿缓存
self.addEventListener('fetch', async(event)=>{
    const cache = await caches.open(CACHE_NAME);
    const req = event.request;
    if(req.method == 'GET'){
        try{
            await event.respondWith(cacheFirst(req, cache));
        }
        catch(err){// 缓存和网络均失败时
            console.error(err);
        }
    }
});

async function cacheFirst(req: Request, cache:Cache):Promise<Response> {
    let res:Response;
    res = await cache.match(req) || await fetch(req);// 当断网时，有可能缓存和网络均失败
    
    // 克隆响应，如果不克隆会有res被占用错误
    const resClone = res.clone();
    if(
        (
            res.status == 200 
            || res.status == 304
        )
        &&
        !noCacheUrlArr.some((item)=>{return req.url.indexOf(item)!=-1}) 
    ){
        // 更新缓存
        cache.put(req, resClone);
    };

    return res;
}