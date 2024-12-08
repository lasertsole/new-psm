import { Manager } from 'socket.io-client';

let socketUrl:string = "ws://localhost:9000";

export const wsManager: Manager= new Manager(socketUrl, {
  reconnection: false // 禁用自动重连
  ,transports: ['websocket'] // 默认是http轮训，设置使用websocket
  , upgrade: false // 关闭自动升级
});