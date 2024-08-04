import ElementPlus from 'element-plus'
export default function useApiFetch<T>(url: string, options?: object):ReturnType<typeof useFetch>{ 
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
            if(raw_data.code != 200){
                ElMessage.error(raw_data.msg);
                localStorage.removeItem('token');
            }
            else if(raw_data.data.token){
                console.log(raw_data);
                localStorage.setItem('token', raw_data.data.token)
            }
        },
        onResponseError({ request, response, options }) {
            // 处理响应错误
        }
    })
}