package com.psm.domain.Model.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.Model.repository.ModelDB;
import com.psm.domain.Model.repository.mapper.ModelMapper;
import com.psm.infrastructure.annotation.spring.Repository;
import com.psm.infrastructure.enums.VisibleEnum;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class ModelDBImpl extends ServiceImpl<ModelMapper, ModelDAO> implements ModelDB {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void insert(ModelDAO modelDAO) { modelMapper.insert(modelDAO); }
    @Override
    public void delete(ModelDAO modelDAO) {
        LambdaQueryWrapper<ModelDAO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ModelDAO::getUserId, modelDAO.getUserId())
                .and(wrapper -> wrapper.eq(ModelDAO::getEntity, modelDAO.getEntity()));

        modelMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public ModelDAO selectById(Long modelId, VisibleEnum visibleEnum) {
        LambdaQueryWrapper<ModelDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.ge(ModelDAO::getVisible, visibleEnum);
        modelWrapper.select(ModelDAO::getId, ModelDAO::getUserId, ModelDAO::getTitle, ModelDAO::getCover, ModelDAO::getCategory,
                ModelDAO::getCreateTime);
        return modelMapper.selectOne(modelWrapper);
    }
}
