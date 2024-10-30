package com.psm.domain.User.user.infrastructure.utils;

import org.springframework.stereotype.Component;

@Component
public class Oauth2UserIdContextHolder {
    private static final ThreadLocal<Long> contextHolder = new ThreadLocal<>();
    public static void setUserId(Long userId) {
        contextHolder.set(userId);
    }

    public static Long getUserId() {
        return contextHolder.get();
    }

    public static void removeUserId() {
        contextHolder.remove();
    }
}
