package com.psm.domain.Independent.User.Single.user.event.bus.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义密码加密工具类
 */
@Slf4j
public class BcryptEncoderUtil {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static boolean isBcrypt(String encodedPassword) {
        return encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2y$");
    }
}
