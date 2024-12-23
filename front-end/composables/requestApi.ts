import * as tus from 'tus-js-client';
import type { AsyncData } from "#app";
import type { Upload } from 'tus-js-client';
import type { NitroFetchRequest } from 'nitropack';
import type { Response, UseFetchResponse } from "@/types/response";

interface Params {
    url: NitroFetchRequest;
    opts?: { [key: string]: any } | FormData;
    method?: 'get' | 'post' | 'put' | 'delete';
    contentType?: 'application/x-www-form-urlencoded' | 'application/json' | 'multipart/form-data';
    lazy?: boolean;
    headeropts?: { [key: string]: any };
}

// 替换路径变量
const replacePathVariables = (url: NitroFetchRequest, params: any = {}) => {
  if (Object.keys(params).length === 0) {
    return url;
  }
  const regex = /\/:(\w+)/gm;
  let formattedURL = url as string;
  let m = regex.exec(formattedURL);
  while (m) {
    if (m.index === regex.lastIndex) {
      regex.lastIndex += 1;
    }
    if (params[m[1]] === undefined) {
      throw new Error(`"${m[1]}" is not provided in params`);
    }
    formattedURL = formattedURL.replace(`:${m[1]}`, params[m[1]]);
    delete params[m[1]];
    m = regex.exec(formattedURL);
  }
  return formattedURL;
};

// 使用$fetch的api请求
export async function fetchApi({
  url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
  headeropts = {},
}: Params): Promise<Response> {
  // 设置请求头
  const headers:any = {};

  // 响应长度
  let responseContentLength:number=0;

  if(import.meta.client&&localStorage.getItem('token')){
    headers.token = localStorage.getItem('token');
  }
  // 设置请求参数
  let params:any = {};
  if(contentType=='application/json'){
    opts = { ...opts };
  }
  
  if (method == 'get') {
    params.query = opts;
  } else {
    params.body = opts;
  }

  // 发出请求
  let res:Response = await $fetch(url,{
    method,
    baseURL: import.meta.env.VITE_API_BACK_URL,
    ...params,
    
    onRequest({ options }:{ options:any }) {
      let token = import.meta.client&&localStorage.getItem('token');
      if(token){
          options.headers = {
            ...options.headers,
            ...headeropts,
            token
          }
      }
      return;
    },

    onResponse({ response }:{ response:any }) {
      if(import.meta.server) return response;

      // 处理响应数据
      // 如果返回值有token，则更新本地token
      let token : string | null = response.headers.get("token");
      if(token){
        localStorage.setItem('token', token);
      };
      return response;
    },
  });

  return res;
};

interface UploadParams {
  url: string;
  file: File;
  progressCB?: Function;
  successCB?: Function;
  errorCB?: Function;
}
export async function tusUploadApi({
  file, 
  url, 
  progressCB,
  successCB,
  errorCB
}:UploadParams): Promise<void> {
  let upload:Upload = new tus.Upload(file, {
    removeFingerprintOnSuccess : true, // 上传成功后删除指纹(localstorage缓存)
    endpoint: import.meta.env.VITE_API_BACK_URL + url,
    headers: {
      token: localStorage.getItem('token')||""
    },
    chunkSize: 1024 * 1024,// 每个分片大小1MB
    retryDelays: [0, 3000, 5000, 10000, 20000],
    metadata: {
      filename: file.name,
      filetype: file.type,
    },
    onError: function (error) {
      errorCB&&errorCB(error);
    },
    onProgress: function (bytesUploaded, bytesTotal) {
      var percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2)
      progressCB&&progressCB(percentage + '%');
    },
    onSuccess: function () {
      if(upload.url==null) return;
      const parts = upload.url.split('/');
      const uuid = parts[parts.length - 1];
      successCB&&successCB();
      upload.abort();
    },
  });
  
  // 查找之前的上传记录
  upload.findPreviousUploads().then((previousUploads)=>{
    // 如果找到了之前的上传记录，则选择第一个记录并调用
    if (previousUploads.length) {
      upload.resumeFromPreviousUpload(previousUploads[0])
    }

    // 开始新的上传操作
    upload.start();
  });
};

function useFetchBaseApi({
  url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
  lazy = false,
}: Params): AsyncData<Response, any> {
  const requestURL = replacePathVariables(url, opts);
  return useFetch(requestURL, {
    method,
    // ofetch库会自动识别请求地址，对于url已包含域名的请求不会再拼接baseURL
    baseURL: import.meta.env.VITE_API_BACK_URL,
    // 是否是懒加载请求
    lazy,
    // onRequest相当于请求拦截
    onRequest({ request, options }) {
      // 设置请求头
      options.headers.set('Content-Type', contentType);
      // 设置请求参数
      if (method === 'post') {
        options.body = { ...opts };
      } else {
        options.query = { ...opts };
      }
      let token = localStorage.getItem('token');
      if(token){
        options.headers.set('token', token);
      }
    },
    onRequestError({ request, options, error }) {
      // Handle the request errors
    },
    // onResponse相当于响应拦截
    onResponse({ response }) {
      // 处理响应数据
      // 如果返回值有token，则更新本地token
      let token : string | null = response.headers.get("token");
      if(token){
        localStorage.setItem('token', token);
      }

      return response;
    },
    onResponseError({ request, response, options }) {
      // Handle the response errors
    }
  });
};

export function useFetchApi({
  url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
}: Params): AsyncData<Response, any> {
  return useFetchBaseApi({
    url,
    opts,
    method,
    contentType,
  });
};

export function useLazyFetchApi({
  url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
}: Params): AsyncData<Response, any> { 
  return useFetchBaseApi({
    url,
    opts,
    method,
    contentType,
    lazy:true,
  });
};