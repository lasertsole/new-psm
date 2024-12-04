export function loadWebAssembly (path:string):Promise<WebAssembly.Instance> {
    return fetch(path) // 加载文件
    .then(response => response.arrayBuffer()) // 转成 ArrayBuffer
    .then(buffer => WebAssembly.compile(buffer))
    .then(module => new WebAssembly.Instance(module));
};