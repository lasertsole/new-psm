package com.psm.domain.Model.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psm.domain.Model.entity.ModelDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModelMapper extends BaseMapper<ModelDAO> {
}
