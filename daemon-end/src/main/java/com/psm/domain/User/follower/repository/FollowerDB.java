package com.psm.domain.User.follower.repository;

import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepository;

import java.util.List;

public interface FollowerDB extends BaseDBRepository<FollowerDAO> {
    /**
     * 添加关注记录
     *
     * @param followerDAO 关注记录DAO
     * @return 关注记录id
     */
    void insertTgUserIdAndSrcUserId(FollowerDAO followerDAO);

    /**
     * 根据源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @return 关注记录DAO列表
     */
    List<FollowerDAO> selectByTgtUserId(Long tgtUserId);

    /**
     * 根据源用户id获取关注记录
     *
     * @param srcUserId 源用户id
     * @return 关注记录DAO列表
     */
    List<FollowerDAO> selectBySrcUserId(Long srcUserId);

    /**
     * 根据目标用户id和源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     * @return 关注记录DAO
     */
    FollowerDAO selectByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId);

    /**
     * 根据目标用户id和源用户id删除关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     */
    void deleteByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId);
}
