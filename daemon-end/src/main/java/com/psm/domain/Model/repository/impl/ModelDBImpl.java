package com.psm.domain.Model.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public Page<ModelDAO> getModelListByIds() {
        return null;
    }

    @Override
    public void delete(ModelDAO modelDAO) {
        LambdaQueryWrapper<ModelDAO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ModelDAO::getUserId, modelDAO.getUserId())
                .and(wrapper -> wrapper.eq(ModelDAO::getEntity, modelDAO.getEntity()));

        modelMapper.delete(lambdaQueryWrapper);
    }
}
