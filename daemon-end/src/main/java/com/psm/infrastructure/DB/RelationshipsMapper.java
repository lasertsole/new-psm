package com.psm.infrastructure.DB;

import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RelationshipsMapper extends BaseDBMapper<RelationshipsDAO> {
}
