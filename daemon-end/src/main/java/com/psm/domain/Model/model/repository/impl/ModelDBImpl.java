package com.psm.domain.Model.model.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.infrastructure.DB.ModelMapper;
import com.psm.domain.Model.model.repository.ModelDB;
import com.psm.app.annotation.spring.Repository;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
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
        LambdaQueryWrapper<ModelDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ModelDAO::getId, modelId);
        wrapper.ge(ModelDAO::getVisible, visibleEnum);
        wrapper.select(ModelDAO::getId, ModelDAO::getUserId, ModelDAO::getTitle, ModelDAO::getCover,
                ModelDAO::getStyle, ModelDAO::getType, ModelDAO::getCreateTime);

        return modelMapper.selectOne(wrapper);
    }

    @Override
    public List<ModelDAO> selectByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        LambdaQueryWrapper<ModelDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.in(ModelDAO::getUserId, userIds);
        modelWrapper.eq(ModelDAO::getVisible, visibleEnum);
        modelWrapper.select(ModelDAO::getId, ModelDAO::getUserId, ModelDAO::getTitle, ModelDAO::getCover,
                ModelDAO::getStyle, ModelDAO::getType, ModelDAO::getCreateTime);
        return modelMapper.selectList(modelWrapper);
    }
}
