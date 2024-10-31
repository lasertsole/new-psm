package com.psm.domain.User.follower.service;

import com.psm.domain.User.follower.entity.FollowerDAO;

import java.util.List;

public interface FollowerService {
    /**
     * 根据源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @return 关注记录DAO列表
     */
    List<FollowerDAO> getByTgtUserId(Long tgtUserId);

    /**
     * 根据来源用户id获取关注记录
     *
     * @param srcUserId 来源用户id
     * @return 关注记录列表
     */
    List<FollowerDAO> getBySrcUserId(Long srcUserId);

    /**
     * 添加关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关注记录id
     */
    Long addFollower(Long tgtUserId, Long srcUserId);

    /**
     * 根据目标用户id和来源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关注记录
     */
    FollowerDAO getByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId);

    /**
     * 根据目标用户id和来源用户id删除关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     */
    void removeByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId);
}
