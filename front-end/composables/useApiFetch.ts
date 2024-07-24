export default function useApiFetch<T>(url: string, options?: object):ReturnType<typeof useFetch>{ 
    return useFetch<T>(`/api/${url}`, {
        ...options,
        onRequest({ request, options }) {
        // 设置请求头
        },
        onRequestError({ request, options, error }) {
        // 处理请求错误
        },
        onResponse({ request, response, options }) {
        // 处理响应数据
        },
        onResponseError({ request, response, options }) {
        // 处理响应错误
        }
    })
}