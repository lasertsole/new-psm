package com.psm.service.User.impl;

import com.psm.domain.User.UserDAO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.User.UserManagerService;
import com.psm.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserManagerServiceImpl implements UserManagerService {
    @Autowired
    private UserService userService;

    @Override
    public ResponseDTO login(UserDAO userDAO) {
        return userService.login(userDAO);
    }

    @Override
    public ResponseDTO logout() {
        return userService.logout();
    }

    @Override
    public ResponseDTO register(UserDAO userDAO) {
        return userService.register(userDAO);
    }

    @Override
    public ResponseDTO deleteUser() {
        return userService.deleteUser();
    }

    @Override
    public ResponseDTO updateUser(UserDAO userDAO) {
        return userService.updateUser(userDAO);
    }

    @Override
    public ResponseDTO updatePassword(String password, String changePassword) {
        return userService.updatePassword(password,changePassword);
    }

    @Override
    public ResponseDTO getUserByID(Long id) {
        return userService.getUserByID(id);
    }

    @Override
    public ResponseDTO getUserByName(String name) {
        return userService.getUserByName(name);
    }
}
