package com.psm.domain.IndependentDomain.User.relationships.service.impl;

import com.psm.domain.IndependentDomain.User.relationships.entity.RelationshipsBO;
import com.psm.domain.IndependentDomain.User.relationships.entity.RelationshipsDO;
import com.psm.domain.IndependentDomain.User.relationships.repository.RelationshipsDB;
import com.psm.domain.IndependentDomain.User.relationships.service.RelationshipsService;
import com.psm.domain.IndependentDomain.User.relationships.types.convertor.RelationshipsConvertor;
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
    public List<RelationshipsBO> checkFollowers(Long tgtUserId) {
        // 创建 FollowerDAO 对象
        RelationshipsDO followerDO = new RelationshipsDO();
        followerDO.setTgtUserId(tgtUserId);
        followerDO.setIsFollowing(true);

        return relationshipsDB.selectByTgtUserId(followerDO).stream().map(RelationshipsConvertor.INSTANCE::DO2BO).toList();
    }

    @Override
    public List<RelationshipsBO> checkFollowing(Long srcUserId) {
        RelationshipsDO relationshipsDO = new RelationshipsDO();
        relationshipsDO.setSrcUserId(srcUserId);
        relationshipsDO.setIsFollowing(true);

        return relationshipsDB.selectBySrcUserId(relationshipsDO).stream().map(RelationshipsConvertor.INSTANCE::DO2BO).toList();
    }

        @Override
    public void saveOrUpdateRelationship(Long tgtUserId, Long srcUserId, Boolean isFollowing, Boolean isInContacts, Boolean isBlocking) {
        RelationshipsDO relationshipsDO = new RelationshipsDO();
        relationshipsDO.setTgtUserId(tgtUserId);
        relationshipsDO.setSrcUserId(srcUserId);
        relationshipsDO.setIsFollowing(isFollowing);
        relationshipsDO.setIsInContacts(isInContacts);
        relationshipsDO.setIsBlocking(isBlocking);

        relationshipsDB.insertOrUpdateRelationship(relationshipsDO);
    };

    @Override
    public RelationshipsBO getRelationship(Long tgtUserId, Long srcUserId) {
        RelationshipsDO relationshipsDO = new RelationshipsDO();
        relationshipsDO.setTgtUserId(tgtUserId);
        relationshipsDO.setSrcUserId(srcUserId);

        return RelationshipsConvertor.INSTANCE.DO2BO(relationshipsDB.selectRelationship(relationshipsDO));
    };

    @Override
    public void following(Long tgtUserId, Long srcUserId) throws DuplicateKeyException {

        // 将FollowerDAO 对象保存到数据库
        saveOrUpdateRelationship(tgtUserId, srcUserId, true, null, null);
    }

    @Override
    public RelationshipsBO checkFollowShip(Long tgtUserId, Long srcUserId) {
        RelationshipsDO relationshipsDO = new RelationshipsDO();
        relationshipsDO.setTgtUserId(tgtUserId);
        relationshipsDO.setSrcUserId(srcUserId);
        relationshipsDO.setIsFollowing(true);

        return RelationshipsConvertor.INSTANCE.DO2BO(relationshipsDB.selectRelationship(relationshipsDO));
    }

    @Override
    public void unFollowing(Long tgtUserId, Long srcUserId) {

        // 将FollowerDAO 对象保存到数据库
        saveOrUpdateRelationship(tgtUserId, srcUserId, false, null, null);
    }
}