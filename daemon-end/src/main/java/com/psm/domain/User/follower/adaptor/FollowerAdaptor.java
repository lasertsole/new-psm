package com.psm.domain.User.follower.adaptor;

import com.psm.domain.User.follower.entity.FollowerBO;
import com.psm.domain.User.follower.entity.FollowerDTO;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface FollowerAdaptor {
    /**
     * 根据源用户id获取关注记录
     *
     * @param tgtUserId 目标用户id
     * @return 关注记录DAO列表
     */
    List<FollowerBO> getByTgtUserId(Long tgtUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 根据目标用户id获取关注列表
     *
     * @param srcUserId 目标用户ID
     * @return 关注列表
     */
    List<FollowerBO> getBySrcUserId(Long srcUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 添加关注记录
     *
     * @param followerDTO 关注信息DTO
     * @return 关注信息ID
     */
    Long addFollowing(@Valid FollowerDTO followerDTO) throws DuplicateKeyException;

    /**
     * 添加关注记录
     *
     * @param tgtUserId 目标用户ID
     * @param srcUserId 源用户ID
     * @return 关注信息ID
     */
    Long addFollowing(Long tgtUserId, Long srcUserId) throws DuplicateKeyException, InstantiationException, IllegalAccessException;

    /**
     * 根据目标用户id和来源用户id获取关注信息
     *
     * @param followerDTO 关注信息DTO
     * @return 关注信息BO
     */
    FollowerBO getByTgUserIdAndSrcUserId(FollowerDTO followerDTO);

    /**
     * 根据目标用户id和来源用户id获取关注信息
     *
     * @param tgtUserId 目标用户ID
     * @param srcUserId 源用户ID
     * @return 关注信息BO
     */
    FollowerBO getByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException;

    /**
     * 判断用户是否关注了目标用户
     *
     * @param followerDTO 关注信息DTO
     * @return 是否关注
     */
    Boolean isFollowed(FollowerDTO followerDTO);

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
    void removeFollowing(FollowerDTO followerDTO);

    /**
     * 根据目标用户id和来源用户id删除关注记录
     *
     * @param tgtUserId 目标用户id
     * @param srcUserId 来源用户id
     */
    void removeFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException;
}