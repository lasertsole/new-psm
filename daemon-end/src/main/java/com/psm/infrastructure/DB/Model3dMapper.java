package com.psm.infrastructure.DB;

import com.psm.domain.Model.model.entity.Model3dDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Model3dMapper extends BaseDBMapper<Model3dDO> {
}
