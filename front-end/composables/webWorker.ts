let cpuCores: number;
const blob:Blob = new Blob([
    `self.onmessage = function (e) {
        postMessage('Hello '+e.data)
    }`],
    {
        type: 'text/javascript'
    }
);

/**
 * 初始化web worker
 */
export async function initWebWorker():Promise<void> {
    cpuCores = navigator.hardwareConcurrency;// 获取cpu核数
};