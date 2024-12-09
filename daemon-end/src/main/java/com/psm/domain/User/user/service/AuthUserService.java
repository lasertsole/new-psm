package com.psm.domain.User.user.service;

import com.psm.domain.User.user.entity.User.UserBO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserService extends UserDetailsService {
    UserBO authUserToken(String token);
}
