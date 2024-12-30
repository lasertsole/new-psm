package com.psm.domain.Independent.User.Single.relationships.repository;

import com.psm.domain.Independent.User.Single.relationships.entity.RelationshipsDO;

import java.util.List;

public interface RelationshipsRepository {
    /**
     * 根据被关注者的id查询所有关注者的信息
     *
     * @param followerDO 关注者的信息
     * @return 关系列表
     */
    List<RelationshipsDO> DBSelectByTgtUserId(RelationshipsDO followerDO);

    /**
     * 根据关注者的id查询所有被关注者的信息
     *
     * @param followerDO 关注者的信息
     * @return 关系列表
     */
    List<RelationshipsDO> DBSelectBySrcUserId(RelationshipsDO followerDO);

    /**
     * 根据关注者和被关注者的id查询关注关系
     *
     * @param followerDO 关注者的信息
     * @return 关系列表
     */
    RelationshipsDO DBSelectRelationship(RelationshipsDO followerDO);

    /**
     * 插入或更新关注关系
     *
     * @param relationshipsDO 关系列表
     */
    void DBInsertOrUpdateRelationship(RelationshipsDO relationshipsDO);
}
