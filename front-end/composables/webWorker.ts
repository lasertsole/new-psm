let cpuCores: number;
const blob:Blob = new Blob([
    `self.onmessage = function (e) {
        postMessage('Hello '+e.data)
    }`],
    {
        type: 'text/javascript'
    }
);
export async function initWebWorker():Promise<void> {
    cpuCores = navigator.hardwareConcurrency;// 获取cpu核数
};