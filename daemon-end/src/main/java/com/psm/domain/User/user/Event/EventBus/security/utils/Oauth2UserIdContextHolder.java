package com.psm.domain.User.user.Event.EventBus.security.utils;

public class Oauth2UserIdContextHolder {
    private static final ThreadLocal<Long> PARAM_THREAD_LOCAL = new ThreadLocal<>();
    public static void setUserId(Long userId) {
        PARAM_THREAD_LOCAL.set(userId);
    }

    public static Long getUserId() {
        return PARAM_THREAD_LOCAL.get();
    }

    public static void removeUserId() {
        PARAM_THREAD_LOCAL.remove();
    }
}
