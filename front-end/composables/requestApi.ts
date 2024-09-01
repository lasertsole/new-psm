import type { NitroFetchRequest } from 'nitropack';

interface Params {
    url: NitroFetchRequest;
    opts?: object;
    method?: 'get' | 'post' | 'put' | 'delete';
    contentType?: 'application/x-www-form-urlencoded' | 'application/json';
}

let loadingCount = 0;

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

export async function getFetchData({
    url,
    opts = {},
    method = 'get',
    contentType = 'application/json',
  }: Params) {
    const requestURL = replacePathVariables(url, opts);
    const { data } = await useFetch(requestURL, {
      method,
      // ofetch库会自动识别请求地址，对于url已包含域名的请求不会再拼接baseURL
      baseURL: "/api",
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
        if (loadingCount === 0) {
          // showLoadingToast({ forbidClick: true });
        }
        let token = localStorage.getItem('token');
        if(token){
            options.headers = {
                ...options.headers,
                token
            }
        }
        loadingCount++;
      },
      // onResponse相当于响应拦截
      onResponse({ response }) {
        // 处理响应数据
        loadingCount--;
        if (loadingCount === 0) {
          // closeToast();
        }
        if (response._data.error) {
          console.warn(
            '=== error url: ',
            url,
            '\n params:',
            opts,
            '\n response:',
            response._data
          );
          // showToast(response._data.message);
        } else {
          return response;
        }
      },
      onRequestError({ request, options, error }) {
        // 处理请求错误
        // console.warn('request error', error);
        // showToast('Request Error');
      },
      onResponseError({ request, response, options }) {
        // 处理响应错误
        // console.warn('request error', response);
        // showToast('Request Error');
      },
    });
    // 这里data本身是个ref对象，将其内部值抛出去方便调用时获得数据。
    return data.value;
}

export function useFetchApi<T>(url: string, options?: object):ReturnType<typeof useFetch>{
    return useFetch<T>(`/api${url}`, {
        ...options,
        onRequest({ request, options }) {
        // 处理请求
            // 设置请求头
            let token =localStorage.getItem('token');
            if(token){
                options.headers = {
                    ...options.headers,
                    token
                }
            }
        },
        onRequestError({ request, options, error }) {
        // 处理请求错误
        },
        onResponse({ request, response, options }) {
            let raw_data = response._data;
            // 处理响应数据
            if(raw_data.code != 200 || raw_data.code != 302){
                ElMessage.error(raw_data.msg);
                localStorage.removeItem('token');
            }
            else if(raw_data.data.token){
                localStorage.setItem('token', raw_data.data.token)
            }
        },
        onResponseError({ request, response, options }) {
            // 处理响应错误
        }
    })
}

export async function useLazyFetchApi<T>(url: string, options?: object){ 
    let { data } = await useLazyFetch<T>(`/api${url}`, {
        ...options,
        onRequest({ request, options }) {
        // 处理请求
            // 设置请求头
            let token =localStorage.getItem('token');
            if(token){
                options.headers = {
                    ...options.headers,
                    token
                }
            }
        },
        onRequestError({ request, options, error }) {
        // 处理请求错误
        },
        onResponse({ request, response, options }) {
            let raw_data = response._data;
            // 处理响应数据
            if(raw_data.code != 200){
                ElMessage.error(raw_data.msg);
                localStorage.removeItem('token');
            }
            else if(raw_data.data.token){
                localStorage.setItem('token', raw_data.data.token)
            }
        },
        onResponseError({ request, response, options }) {
            // 处理响应错误
        }
    })
    return data;
}