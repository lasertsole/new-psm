package com.psm.domain.User.follower.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.repository.FollowerDB;
import com.psm.domain.User.follower.repository.mapper.FollowerMapper;
import com.psm.infrastructure.annotation.spring.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Repository
public class FollowerDBImpl extends ServiceImpl<FollowerMapper, FollowerDAO> implements FollowerDB {
    @Autowired
    private FollowerMapper followerMapper;

    @Override
    public List<FollowerDAO> selectByTgtUserId(Long tgtUserId) {
        LambdaQueryWrapper<FollowerDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowerDAO::getTgtUserId,tgtUserId);

        return followerMapper.selectList(wrapper);
    }

    @Override
    public List<FollowerDAO> selectBySrcUserId(Long srcUserId) {
        LambdaQueryWrapper<FollowerDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowerDAO::getSrcUserId,srcUserId);

        return followerMapper.selectList(wrapper);
    }

    @Override
    public FollowerDAO selectByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        LambdaQueryWrapper<FollowerDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowerDAO::getTgtUserId,tgtUserId).and(
            w->w.eq(FollowerDAO::getSrcUserId,srcUserId));

        return followerMapper.selectOne(wrapper);
    }

    @Override
    public void deleteByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        LambdaQueryWrapper<FollowerDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowerDAO::getTgtUserId,tgtUserId).and(
            w->w.eq(FollowerDAO::getSrcUserId,srcUserId));

        followerMapper.delete(wrapper);
    }
}
