package com.psm.domain.User.infrastructure.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义密码加密工具类
 */
public class CustomPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 检查密码是否是 Bcrypt 类型
        if (isBcrypt(encodedPassword)) {
            return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        } else {
            // 如果不是 Bcrypt 类型，返回 false
            return false;
        }
    }

    public static boolean isBcrypt(String encodedPassword) {
        return encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2y$");
    }
}
