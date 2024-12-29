package com.psm.domain.Independent.User.Single.relationships.adaptor;

import com.psm.domain.Independent.User.Single.relationships.entity.RelationshipsBO;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface RelationshipsAdaptor {
    /**
     * 根据源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @return  关系实体BO列表
     */
    List<RelationshipsBO> checkFollowers(Long tgtUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 根据目标用户id获取关注列表
     *
     * @param srcUserId 目标用户ID
     * @return 关注列表
     */
    List<RelationshipsBO> checkFollowing(Long srcUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 添加关注记录
     *
     * @param relationshipsBO 关系实体BO
     */
    void addFollowing(@Valid RelationshipsBO relationshipsBO) throws DuplicateKeyException;

    /**
     * 添加关注记录
     *
     * @param tgtUserId 目标用户ID
     * @param srcUserId 源用户ID
     */
    void addFollowing(Long tgtUserId, Long srcUserId) throws DuplicateKeyException, InstantiationException, IllegalAccessException;

    /**
     * 根据目标用户id和来源用户id获取关注信息
     *
     * @param relationshipsBO 关系实体BO
     * @return 关注信息BO
     */
    RelationshipsBO checkFollowShip(RelationshipsBO relationshipsBO);

    /**
     * 根据目标用户id和来源用户id获取关注信息
     *
     * @param tgtUserId 目标用户ID
     * @param srcUserId 源用户ID
     * @return 关注信息BO
     */
    RelationshipsBO checkFollowShip(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 判断用户是否关注了目标用户
     *
     * @param relationshipsBO 关系实体BO
     * @return 是否关注
     */
    Boolean isFollowed(RelationshipsBO relationshipsBO);

    /**
     * 判断用户是否关注了目标用户
     *
     * @param tgtUserId 目标用户ID
     * @param srcUserId 源用户ID
     * @return 是否关注
     */
    Boolean isFollowed(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 根据目标用户id和来源用户id删除关注记录
     *
     * @param relationshipsBO 关系实体BO
     */
    void removeFollowing(RelationshipsBO relationshipsBO);

    /**
     * 根据目标用户id和来源用户id删除关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     */
    void removeFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 更新或保存关系记录
     * @param relationshipsBO 关系实体BO
     */
    void saveOrUpdateRelationship(RelationshipsBO relationshipsBO);

    /**
     * 根据条件查询关系记录
     * @param relationshipsBO 关系实体BO
     * @return 关系记录
     */
    RelationshipsBO getRelationship(RelationshipsBO relationshipsBO);
}