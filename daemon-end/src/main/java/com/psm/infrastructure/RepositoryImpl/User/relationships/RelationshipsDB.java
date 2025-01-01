package com.psm.infrastructure.RepositoryImpl.User.relationships;

import com.psm.domain.Independent.User.Single.relationships.pojo.entity.RelationshipsDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepository;

import java.util.List;

public interface RelationshipsDB extends BaseDBRepository<RelationshipsDO> {
    /**
     * 根据源用户id获取关系记录
     *
     * @param relationshipsDO 关系记录
     * @return 关系记录DAO列表
     */
    List<RelationshipsDO> selectByTgtUserId(RelationshipsDO relationshipsDO);

    /**
     * 根据源用户id获取关系记录
     *
     * @param relationshipsDO 关系记录
     * @return 关系记录DAO列表
     */
    List<RelationshipsDO> selectBySrcUserId(RelationshipsDO relationshipsDO);

    /**
     * 根据目标用户id和源用户id获取关系记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关系记录DAO
     */
    RelationshipsDO selectByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId);

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
     * @param relationshipsDO 关系记录
     */
    void insertOrUpdateRelationship(RelationshipsDO relationshipsDO);

    /**
     * 查询关系记录
     *
     * @param relationshipsDO 关系记录
     * @return RelationshipsDO
     */
    RelationshipsDO selectRelationship(RelationshipsDO relationshipsDO);
}
