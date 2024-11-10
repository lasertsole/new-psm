import io from 'socket.io-client';
import { defineNuxtPlugin } from '#app'

export default defineNuxtPlugin(() => {
    const socket = io("http://localhost:8001");
    return {
        provide: {
            socket
        }
    };
});