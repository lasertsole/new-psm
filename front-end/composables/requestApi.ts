import type { NitroFetchRequest } from 'nitropack';
import { useRuntimeConfig } from '#app';
import * as tus from 'tus-js-client' // 假设 tus-js-client 是你使用的库
import type { Upload } from 'tus-js-client';

interface Params {
    url: NitroFetchRequest;
    opts?: { [key: string]: any } | FormData;
    method?: 'get' | 'post' | 'put' | 'delete';
    contentType?: 'application/x-www-form-urlencoded' | 'application/json' | 'multipart/form-data';
    lazy?: boolean;
}

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
}: Params){

  // 设置请求头
  const headers:any = {};
  if(localStorage.getItem('token')){
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
  let res = await $fetch(url,{
    method,
    baseURL: "/api",
    ...params,
    onRequest({ options }:{ options:any }) {
      let token = localStorage.getItem('token');
      if(token){
          options.headers = {
              ...options.headers,
              token
          }
      }
    },
    onResponse({ response }:{ response:any }) {
      // 处理响应数据
      // 如果返回值有token，则更新本地token
      let token : string | null = response.headers.get("token");
      if(token){
        localStorage.setItem('token', token);
      }

      return response;
    },
  });

  return res;
};

export async function tusUploadApi(file: File, url: string){
  let upload:Upload = new tus.Upload(file, {
    endpoint: import.meta.env.VITE_API_BASE_URL + url,
    headers: {
      token: localStorage.getItem('token')||""
    },
    retryDelays: [0, 3000, 5000, 10000, 20000],
    metadata: {
      filename: file.name,
      filetype: file.type,
    },
    onError: function (error) {
      console.log('Failed because: ' + error)
    },
    onProgress: function (bytesUploaded, bytesTotal) {
      var percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2)
      console.log(bytesUploaded, bytesTotal, percentage + '%')
    },
    onSuccess: function () {
      console.log('Download %s from %s', file.name, upload.url)
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
}