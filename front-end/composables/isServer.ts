import { useRuntimeConfig } from 'nuxt/app'

export function useIsServerSide() {
  const isServer = useRuntimeConfig().public.isServer

  return { isServer }
}