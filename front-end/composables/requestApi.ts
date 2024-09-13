import type { NitroFetchRequest } from 'nitropack';

interface Params {
    url: NitroFetchRequest;
    opts?: object;
    method?: 'get' | 'post' | 'put' | 'delete';
    contentType?: 'application/x-www-form-urlencoded' | 'application/json';
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

export async function useFetchBaseApi({
  url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
  lazy = false,
}: Params) {
  const requestURL = replacePathVariables(url, opts);
  const { data } = await useFetch(requestURL, {
    method,
    // ofetch库会自动识别请求地址，对于url已包含域名的请求不会再拼接baseURL
    baseURL: "/api",
    // 是否是懒加载请求
    lazy,
    // onRequest相当于请求拦截
    onRequest({ request, options }) {
      // 设置请求头
      options.headers = { 'Content-Type': contentType };
      // 设置请求参数
      if (method === 'post') {
        options.body = { ...opts };
      } else {
        options.query = { ...opts };
      }
      let token = localStorage.getItem('token');
      if(token){
          options.headers = {
              ...options.headers,
              token
          }
      }
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
  });
  // 这里data本身是个ref对象，将其内部值抛出去方便调用时获得数据。
  return data.value;
};

export async function useFetchApi({
    url,
    opts = {},
    method = 'get',
    contentType = 'application/json',
  }: Params) {
  return await useFetchBaseApi({
    url,
    opts,
    method,
    contentType,
  });
};

export async function useLazyFetchApi<T>({
  url,
  opts = {},
  method = 'get',
  contentType = 'application/json',
}: Params){ 
  return await useFetchBaseApi({
    url,
    opts,
    method,
    contentType,
    lazy:true,
  });
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
  opts = { ...opts };
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
    contentType,
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