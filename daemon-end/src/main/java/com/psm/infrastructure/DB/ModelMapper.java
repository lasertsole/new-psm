package com.psm.infrastructure.DB;

import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.Model.model.entity.ModelDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModelMapper extends MPJBaseMapper<ModelDAO> {
}
