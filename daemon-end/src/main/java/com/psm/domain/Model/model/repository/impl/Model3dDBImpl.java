package com.psm.domain.Model.model.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.infrastructure.DB.Model3dMapper;
import com.psm.domain.Model.model.repository.Model3dDB;
import com.psm.app.annotation.spring.Repository;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Repository
public class Model3dDBImpl extends BaseDBRepositoryImpl<Model3dMapper, Model3dDAO> implements Model3dDB {
    @Autowired
    private Model3dMapper model3dMapper;

    @Override
    public void insert(Model3dDAO model3dDAO) { model3dMapper.insert(model3dDAO); }
    @Override
    public void delete(Model3dDAO model3dDAO) {
        LambdaQueryWrapper<Model3dDAO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Model3dDAO::getUserId, model3dDAO.getUserId())
                .and(wrapper -> wrapper.eq(Model3dDAO::getEntity, model3dDAO.getEntity()));

        model3dMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public Model3dDAO selectById(Long modelId, VisibleEnum visibleEnum) {
        LambdaQueryWrapper<Model3dDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Model3dDAO::getId, modelId);
        wrapper.ge(Model3dDAO::getVisible, visibleEnum);
        wrapper.select(Model3dDAO::getId, Model3dDAO::getUserId, Model3dDAO::getTitle, Model3dDAO::getCover,
                Model3dDAO::getStyle, Model3dDAO::getType, Model3dDAO::getCreateTime);

        return model3dMapper.selectOne(wrapper);
    }

    @Override
    public List<Model3dDAO> selectByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        LambdaQueryWrapper<Model3dDAO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.in(Model3dDAO::getUserId, userIds);
        modelWrapper.eq(Model3dDAO::getVisible, visibleEnum);
        modelWrapper.select(Model3dDAO::getId, Model3dDAO::getUserId, Model3dDAO::getTitle, Model3dDAO::getCover,
                Model3dDAO::getStyle, Model3dDAO::getType, Model3dDAO::getCreateTime);

        return model3dMapper.selectList(modelWrapper);
    }
}
