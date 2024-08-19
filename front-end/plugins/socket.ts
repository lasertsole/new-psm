import io from 'socket.io-client';
import { defineNuxtPlugin } from '#app';

export default defineNuxtPlugin(() => {
  const socket = io("/api", {
    transports: ['websocket'],
  })

  return {
    provide: {
      socket
    }
  }
});