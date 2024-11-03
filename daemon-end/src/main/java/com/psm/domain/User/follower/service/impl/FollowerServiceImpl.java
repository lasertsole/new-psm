package com.psm.domain.User.follower.service.impl;

import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.repository.FollowerDB;
import com.psm.domain.User.follower.service.FollowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FollowerServiceImpl implements FollowerService {
    @Autowired
    private FollowerDB followerDB;

    @Override
    public List<FollowerDAO> getByTgtUserId(Long tgtUserId) {
        return followerDB.selectByTgtUserId(tgtUserId);
    }

    @Override
    public List<FollowerDAO> getBySrcUserId(Long srcUserId) {
        return followerDB.selectBySrcUserId(srcUserId);
    }

    @Override
    public Long addFollower(Long tgtUserId, Long srcUserId) throws DuplicateKeyException {
        // 创建 FollowerDAO 对象
        FollowerDAO followerDAO = new FollowerDAO();
        followerDAO.setTgtUserId(tgtUserId);
        followerDAO.setSrcUserId(srcUserId);

        // 将FollowerDAO 对象保存到数据库
        followerDB.save(followerDAO);
        return followerDAO.getId();
    }

    @Override
    public FollowerDAO getByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        return followerDB.selectByTgUserIdAndSrcUserId(tgtUserId, srcUserId);
    }

    @Override
    public void removeByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        followerDB.deleteByTgUserIdAndSrcUserId(tgtUserId, srcUserId);
    }
}