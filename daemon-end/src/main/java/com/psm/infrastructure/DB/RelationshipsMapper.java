package com.psm.infrastructure.DB;

import com.psm.domain.Independent.User.Single.relationships.pojo.entity.RelationshipsDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RelationshipsMapper extends BaseDBMapper<RelationshipsDO> {
}
