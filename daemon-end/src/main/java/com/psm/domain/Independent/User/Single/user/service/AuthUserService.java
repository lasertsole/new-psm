package com.psm.domain.Independent.User.Single.user.service;

import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserBO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserService extends UserDetailsService {
    UserBO authUserToken(String token);
}
