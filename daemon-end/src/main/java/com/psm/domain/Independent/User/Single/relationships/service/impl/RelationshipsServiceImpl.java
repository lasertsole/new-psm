package com.psm.domain.Independent.User.Single.relationships.service.impl;

import com.psm.domain.Independent.User.Single.relationships.entity.RelationshipsBO;
import com.psm.domain.Independent.User.Single.relationships.entity.RelationshipsDO;
import com.psm.domain.Independent.User.Single.relationships.repository.RelationshipsRepository;
import com.psm.domain.Independent.User.Single.relationships.service.RelationshipsService;
import com.psm.domain.Independent.User.Single.relationships.types.convertor.RelationshipsConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RelationshipsServiceImpl implements RelationshipsService {

    @Autowired
    private RelationshipsRepository relationshipsRepository;

    @Override
    public List<RelationshipsBO> checkFollowers(Long tgtUserId) {
        // 创建 FollowerDAO 对象
        RelationshipsDO followerDO = new RelationshipsDO();
        followerDO.setTgtUserId(tgtUserId);
        followerDO.setIsFollowing(true);

        return relationshipsRepository.DBSelectByTgtUserId(followerDO).stream().map(RelationshipsConvertor.INSTANCE::DO2BO).toList();
    }

    @Override
    public List<RelationshipsBO> checkFollowing(Long srcUserId) {
        RelationshipsDO relationshipsDO = new RelationshipsDO();
        relationshipsDO.setSrcUserId(srcUserId);
        relationshipsDO.setIsFollowing(true);

        return relationshipsRepository.DBSelectBySrcUserId(relationshipsDO).stream().map(RelationshipsConvertor.INSTANCE::DO2BO).toList();
    }

    @Override
    public void saveOrUpdateRelationship(Long tgtUserId, Long srcUserId, Boolean isFollowing, Boolean isInContacts, Boolean isBlocking) {
        RelationshipsDO relationshipsDO = new RelationshipsDO();
        relationshipsDO.setTgtUserId(tgtUserId);
        relationshipsDO.setSrcUserId(srcUserId);
        relationshipsDO.setIsFollowing(isFollowing);
        relationshipsDO.setIsInContacts(isInContacts);
        relationshipsDO.setIsBlocking(isBlocking);

        relationshipsRepository.DBInsertOrUpdateRelationship(relationshipsDO);
    };

    @Override
    public RelationshipsBO getRelationship(Long tgtUserId, Long srcUserId) {
        RelationshipsDO relationshipsDO = new RelationshipsDO();
        relationshipsDO.setTgtUserId(tgtUserId);
        relationshipsDO.setSrcUserId(srcUserId);

        return RelationshipsConvertor.INSTANCE.DO2BO(relationshipsRepository.DBSelectRelationship(relationshipsDO));
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

        return RelationshipsConvertor.INSTANCE.DO2BO(relationshipsRepository.DBSelectRelationship(relationshipsDO));
    }

    @Override
    public void unFollowing(Long tgtUserId, Long srcUserId) {

        // 将FollowerDAO 对象保存到数据库
        saveOrUpdateRelationship(tgtUserId, srcUserId, false, null, null);
    }
}