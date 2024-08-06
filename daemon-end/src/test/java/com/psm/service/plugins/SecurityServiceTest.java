package com.psm.service.plugins;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class SecurityServiceTest {
    @Test
    public void testBcryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword1 = passwordEncoder.encode(password);
        String encodedPassword2 = passwordEncoder.encode(password);
        System.out.println(encodedPassword1);
        System.out.println(encodedPassword2);

        System.out.println(passwordEncoder.matches(password, encodedPassword1));
        System.out.println(passwordEncoder.matches("password", encodedPassword1));
    }
}
