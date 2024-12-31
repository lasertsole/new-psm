import * as tus from 'tus-js-client';
import type { Upload } from 'tus-js-client';
import type { NitroFetchRequest } from 'nitropack';
import type { Response, UseFetchResponse } from "@/types/response";
import type { FetchResponse } from 'ofetch';

interface Params {
    url: NitroFetchRequest;
    opts?: { [key: string]: any } | FormData;
    method?: 'get' | 'post' | 'put' | 'delete';
    contentType?: 'application/x-www-form-urlencoded' | 'application/json' | 'multipart/form-data';
    lazy?: boolean;
    headeropts?: { [key: string]: any };
    onError?: () => void;
    initialCache?: boolean;
    server?: boolean;
    watch?: [];
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

interface UploadParams {
  url: string;
  file: File;
  progressCB?: Function;
  successCB?: Function;
  errorCB?: Function;
};

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
    chunkSize: 1024 * 1024 * 10,// 每个分片大小1MB
    retryDelays: [0, 3000, 5000, 10000, 20000],
    metadata: {
      filename: file.name,
      filetype: file.type,
    },
    onError: function (error) {
      console.error(error);
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

async function useFetchBaseApi({
  url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
  headeropts = {},
  server = true,
  watch = [],
}: Params):Promise<Response> {
  const requestURL = replacePathVariables(url, opts);

  // 设置请求参数
  let params:any = {};
  if(contentType=='application/json'){
    opts = { ...opts };
  };
  
  if (method == 'get') {
    params.query = opts;
  } else {
    params.body = opts;
  };
  
  const {data} = await useFetch(requestURL, {
    method,
    // ofetch库会自动识别请求地址，对于url已包含域名的请求不会再拼接baseURL
    baseURL: import.meta.env.VITE_API_BACK_URL,
    ...params,
    server,
    watch,
    retry: 3,
    retryDelay: 2000,
    // onRequest相当于请求拦截
    onRequest({ request, options }) {
      // 设置请求头
      options.headers.set('Content-Type', contentType);
      for (const [key, value] of Object.entries(headeropts)) {
        options.headers.set(key, value);
      };
      
      if(import.meta.client) {
        let token = localStorage.getItem('token');
        if(token){
          options.headers.set('token', token);
        };
      }
    },

    onRequestError({ request, options, error }) {
      // Handle the request errors
    },
    
    // onResponse相当于响应拦截
    onResponse({ response }) {
      // 处理响应数据
      if(import.meta.client) {
        // 如果返回值有token，则更新本地token
        let token : string | null = response.headers.get("token");
        if(token){
          localStorage.setItem('token', token);
        };
  
        return response;
      };
    },

    onResponseError({ request, response, options }) {
      // Handle the response errors
    }
  });

  return data.value as Response;
};

export async function fetchApi({url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
  headeropts = {},
}: Params): Promise<Response> {
  return useFetchBaseApi({url,
    opts,
    method,
    contentType,
    headeropts,
  });
};