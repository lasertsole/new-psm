package com.psm.domain.User.relationships.adaptor;

import com.psm.domain.User.relationships.entity.RelationshipsBO;
import com.psm.domain.User.relationships.entity.RelationshipsDTO;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface RelationshipsAdaptor {
    /**
     * 根据源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @return 关注记录DAO列表
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
     * @param followerDTO 关注信息DTO
     */
    void addFollowing(@Valid RelationshipsDTO followerDTO) throws DuplicateKeyException;

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
     * @param followerDTO 关注信息DTO
     * @return 关注信息BO
     */
    RelationshipsBO checkFollowShip(RelationshipsDTO followerDTO);

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
     * @param followerDTO 关注信息DTO
     * @return 是否关注
     */
    Boolean isFollowed(RelationshipsDTO followerDTO);

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
     * @param followerDTO 关注信息DTO
     */
    void removeFollowing(RelationshipsDTO followerDTO);

    /**
     * 根据目标用户id和来源用户id删除关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     */
    void removeFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 更新或保存关系记录
     * @param relationshipsDTO 关系记录
     */
    void saveOrUpdateRelationship(RelationshipsDTO relationshipsDTO);

    /**
     * 根据条件查询关系记录
     * @param relationshipsDTO 关系记录
     * @return 关系记录
     */
    RelationshipsBO getRelationship(RelationshipsDTO relationshipsDTO);
}