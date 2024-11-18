package com.psm.domain.Model.model.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.Model.model.entity.Model3dDO;
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
public class Model3dDBImpl extends BaseDBRepositoryImpl<Model3dMapper, Model3dDO> implements Model3dDB {
    @Autowired
    private Model3dMapper model3dMapper;

    @Override
    public void insert(Model3dDO model3dDO) { model3dMapper.insert(model3dDO); }
    @Override
    public void delete(Model3dDO model3dDO) {
        LambdaQueryWrapper<Model3dDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Model3dDO::getUserId, model3dDO.getUserId())
                .and(wrapper -> wrapper.eq(Model3dDO::getEntity, model3dDO.getEntity()));

        model3dMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public Model3dDO selectById(Long modelId, VisibleEnum visibleEnum) {
        LambdaQueryWrapper<Model3dDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Model3dDO::getId, modelId);
        wrapper.ge(Model3dDO::getVisible, visibleEnum);
        wrapper.select(Model3dDO::getId, Model3dDO::getUserId, Model3dDO::getTitle, Model3dDO::getCover,
                Model3dDO::getStyle, Model3dDO::getType, Model3dDO::getCreateTime);

        return model3dMapper.selectOne(wrapper);
    }

    @Override
    public List<Model3dDO> selectByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        LambdaQueryWrapper<Model3dDO> modelWrapper = new LambdaQueryWrapper<>();
        modelWrapper.in(Model3dDO::getUserId, userIds);
        modelWrapper.eq(Model3dDO::getVisible, visibleEnum);
        modelWrapper.select(Model3dDO::getId, Model3dDO::getUserId, Model3dDO::getTitle, Model3dDO::getCover,
                Model3dDO::getStyle, Model3dDO::getType, Model3dDO::getCreateTime);

        return model3dMapper.selectList(modelWrapper);
    }
}
