package com.psm.domain.User.relationships.repository;

import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepository;

import java.util.List;

public interface RelationshipsDB extends BaseDBRepository<RelationshipsDAO> {
    /**
     * 根据源用户id获取关系记录
     *
     * @param relationshipsDAO 关系记录
     * @return 关系记录DAO列表
     */
    List<RelationshipsDAO> selectByTgtUserId(RelationshipsDAO relationshipsDAO);

    /**
     * 根据源用户id获取关系记录
     *
     * @param relationshipsDAO 关系记录
     * @return 关系记录DAO列表
     */
    List<RelationshipsDAO> selectBySrcUserId(RelationshipsDAO relationshipsDAO);

    /**
     * 根据目标用户id和源用户id获取关系记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关系记录DAO
     */
    RelationshipsDAO selectByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId);

    /**
     * 根据目标用户id和源用户id删除关系记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     */
    void deleteByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId);

    /**
     * 更新或保存关系记录
     *
     * @param relationshipsDAO 关系记录
     */
    void insertOrUpdateRelationship(RelationshipsDAO relationshipsDAO);

    /**
     * 查询关系记录
     *
     * @param relationshipsDAO 关系记录
     * @return RelationshipsDAO
     */
    RelationshipsDAO selectRelationship(RelationshipsDAO relationshipsDAO);
}
