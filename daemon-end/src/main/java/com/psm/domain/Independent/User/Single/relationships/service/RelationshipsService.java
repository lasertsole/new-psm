package com.psm.domain.Independent.User.Single.relationships.service;

import com.psm.domain.Independent.User.Single.relationships.pojo.entity.RelationshipsBO;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface RelationshipsService {
    /**
     * 根据源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @return 关注记录BO列表
     */
    List<RelationshipsBO> checkFollowers(Long tgtUserId);

    /**
     * 根据来源用户id获取关注记录
     *
     * @param srcUserId 来源用户id
     * @return 关注记录列表
     */
    List<RelationshipsBO> checkFollowing(Long srcUserId);

    /**
     * 更新或保存关系记录
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @param isFollowing 是否关注
     * @param isInContacts 是否在聊天列表内显示
     * @param isBlocking 是否屏蔽
     */
    void saveOrUpdateRelationship(Long tgtUserId, Long srcUserId, Boolean isFollowing, Boolean isInContacts, Boolean isBlocking);

    /**
     * 根据条件查询关系记录
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关系记录
     */
    RelationshipsBO getRelationship(Long tgtUserId, Long srcUserId);

    /**
     * 添加关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关注记录id
     */
    void following(Long tgtUserId, Long srcUserId) throws DuplicateKeyException;

    /**
     * 根据目标用户id和来源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关注记录
     */
    RelationshipsBO checkFollowShip(Long tgtUserId, Long srcUserId);

    /**
     * 根据目标用户id和来源用户id删除关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     */
    void unFollowing(Long tgtUserId, Long srcUserId);
}
