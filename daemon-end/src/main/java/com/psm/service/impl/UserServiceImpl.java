package com.psm.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.User;
import com.psm.mapper.UserMapper;
import com.psm.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
