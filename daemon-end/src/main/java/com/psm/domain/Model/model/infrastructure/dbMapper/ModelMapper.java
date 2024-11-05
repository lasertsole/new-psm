package com.psm.domain.Model.model.infrastructure.dbMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psm.domain.Model.model.entity.ModelDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModelMapper extends BaseMapper<ModelDAO> {
}
