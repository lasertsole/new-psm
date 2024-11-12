package com.psm.domain.User.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserService extends UserDetailsService {
    String authUserToken(String token);
}
