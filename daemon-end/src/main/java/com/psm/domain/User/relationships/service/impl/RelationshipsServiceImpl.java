package com.psm.domain.User.relationships.service.impl;

import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.domain.User.relationships.repository.RelationshipsDB;
import com.psm.domain.User.relationships.service.RelationshipsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RelationshipsServiceImpl implements RelationshipsService {
    @Autowired
    private RelationshipsDB relationshipsDB;

    @Override
    public List<RelationshipsDAO> checkFollowers(Long tgtUserId) {
        // 创建 FollowerDAO 对象
        RelationshipsDAO followerDAO = new RelationshipsDAO();
        followerDAO.setTgtUserId(tgtUserId);
        followerDAO.setIsFollowing(true);
        return relationshipsDB.selectByTgtUserId(followerDAO);
    }

    @Override
    public List<RelationshipsDAO> checkFollowing(Long srcUserId) {
        RelationshipsDAO relationshipsDAO = new RelationshipsDAO();
        relationshipsDAO.setSrcUserId(srcUserId);
        relationshipsDAO.setIsFollowing(true);

        return relationshipsDB.selectBySrcUserId(relationshipsDAO);
    }

        @Override
    public void saveOrUpdateRelationship(RelationshipsDAO relationshipsDAO) {
        relationshipsDB.insertOrUpdateRelationship(relationshipsDAO);
    };

    @Override
    public RelationshipsDAO getRelationship(RelationshipsDAO relationshipsDAO) {
        return relationshipsDB.selectRelationship(relationshipsDAO);
    };

    @Override
    public void following(Long tgtUserId, Long srcUserId) throws DuplicateKeyException {
        // 创建 FollowerDAO 对象
        RelationshipsDAO followerDAO = new RelationshipsDAO();
        followerDAO.setTgtUserId(tgtUserId);
        followerDAO.setSrcUserId(srcUserId);
        followerDAO.setIsFollowing(true);

        // 将FollowerDAO 对象保存到数据库
        saveOrUpdateRelationship(followerDAO);
    }

    @Override
    public RelationshipsDAO checkFollowShip(Long tgtUserId, Long srcUserId) {
        RelationshipsDAO relationshipsDAO = new RelationshipsDAO();
        relationshipsDAO.setTgtUserId(tgtUserId);
        relationshipsDAO.setSrcUserId(srcUserId);
        relationshipsDAO.setIsFollowing(true);

        return relationshipsDB.selectRelationship(relationshipsDAO);
    }

    @Override
    public void unFollowing(Long tgtUserId, Long srcUserId) {
        // 创建 FollowerDAO 对象
        RelationshipsDAO followerDAO = new RelationshipsDAO();
        followerDAO.setTgtUserId(tgtUserId);
        followerDAO.setSrcUserId(srcUserId);
        followerDAO.setIsFollowing(false);

        // 将FollowerDAO 对象保存到数据库
        saveOrUpdateRelationship(followerDAO);
    }
}