package com.psm.domain.Model.modelsUserBind.repository.impl;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Model.modelsUserBind.repository.modelsUserBindDB;
import com.psm.infrastructure.DB.ModelMapper;
import com.psm.infrastructure.DB.UserExtensionMapper;
import com.psm.infrastructure.DB.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class modelsUserBindDBImpl implements modelsUserBindDB {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserExtensionMapper userExtensionMapper;

    @Autowired
    UserMapper userMapper;


}
