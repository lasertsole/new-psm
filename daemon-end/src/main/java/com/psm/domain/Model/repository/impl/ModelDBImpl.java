package com.psm.domain.Model.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.Model.repository.ModelDB;
import com.psm.domain.Model.repository.mapper.ModelMapper;
import com.psm.infrastructure.annotation.spring.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class ModelDBImpl extends ServiceImpl<ModelMapper, ModelDAO> implements ModelDB {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void insert(ModelDAO modelDAO) { modelMapper.insert(modelDAO); }
}
