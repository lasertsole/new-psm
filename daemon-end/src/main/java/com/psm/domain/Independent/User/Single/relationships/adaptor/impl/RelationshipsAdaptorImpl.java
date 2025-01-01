package com.psm.domain.Independent.User.Single.relationships.adaptor.impl;

import com.psm.domain.Independent.User.Single.relationships.adaptor.RelationshipsAdaptor;
import com.psm.domain.Independent.User.Single.relationships.pojo.entity.RelationshipsBO;
import com.psm.domain.Independent.User.Single.relationships.pojo.entity.RelationshipsDTO;
import com.psm.domain.Independent.User.Single.relationships.service.RelationshipsService;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.utils.Valid.ValidUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Adaptor
public class RelationshipsAdaptorImpl implements RelationshipsAdaptor {
    @Autowired
    private ValidUtil validUtil;

    @Autowired
    private RelationshipsService relationshipsService;

    @Override
    public List<RelationshipsBO> checkFollowers(Long tgtUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId), RelationshipsDTO.class);

        return relationshipsService.checkFollowers(tgtUserId);
    }

    @Override
    public List<RelationshipsBO> checkFollowing(Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("srcUserId", srcUserId), RelationshipsDTO.class);

        return relationshipsService.checkFollowing(srcUserId);
    }

    @Override
    public void addFollowing(@Valid RelationshipsBO relationshipsBO) {
        if(
                Objects.isNull(relationshipsBO.getTgtUserId())
                || Objects.isNull(relationshipsBO.getSrcUserId())
                || relationshipsBO.getTgtUserId().equals(relationshipsBO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        relationshipsService.following(relationshipsBO.getTgtUserId(), relationshipsBO.getSrcUserId());
    }

    @Override
    public void addFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), RelationshipsDTO.class);

        if(tgtUserId.equals(srcUserId)) throw new InvalidParameterException("Invalid parameter");

        relationshipsService.following(tgtUserId, srcUserId);
    }

    @Override
    public RelationshipsBO checkFollowShip(@Valid RelationshipsBO relationshipsBO) {
        if(
                Objects.isNull(relationshipsBO.getTgtUserId())
                ||Objects.isNull(relationshipsBO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return relationshipsService.checkFollowShip(relationshipsBO.getTgtUserId(), relationshipsBO.getSrcUserId());
    }

    @Override
    public RelationshipsBO checkFollowShip(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), RelationshipsDTO.class);

        return relationshipsService.checkFollowShip(tgtUserId, srcUserId);
    }

    @Override
    public Boolean isFollowed(@Valid RelationshipsBO relationshipsBO) {
        if(
                Objects.isNull(relationshipsBO.getTgtUserId())
                || Objects.isNull(relationshipsBO.getSrcUserId())
                || relationshipsBO.getTgtUserId().equals(relationshipsBO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return Objects.nonNull(relationshipsService.checkFollowShip(relationshipsBO.getTgtUserId(), relationshipsBO.getSrcUserId()));
    }

    @Override
    public Boolean isFollowed(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        return Objects.nonNull(relationshipsService.checkFollowShip(tgtUserId, srcUserId));
    }

    @Override
    public void removeFollowing(@Valid RelationshipsBO relationshipsBO) {
        if(
                Objects.isNull(relationshipsBO.getTgtUserId())
                ||Objects.isNull(relationshipsBO.getSrcUserId())
                || relationshipsBO.getTgtUserId().equals(relationshipsBO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        relationshipsService.unFollowing(relationshipsBO.getTgtUserId(), relationshipsBO.getSrcUserId());
    }

    @Override
    public void removeFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), RelationshipsDTO.class);

        if(tgtUserId.equals(srcUserId)) throw new InvalidParameterException("Invalid parameter");

        relationshipsService.unFollowing(tgtUserId, srcUserId);
    }

    @Override
    public void saveOrUpdateRelationship(@Valid RelationshipsBO relationshipsBO) {
        Long tgtUserId = relationshipsBO.getTgtUserId();
        Long srcUserId = relationshipsBO.getSrcUserId();
        Boolean isFollowing = relationshipsBO.getIsFollowing();
        Boolean isInContacts = relationshipsBO.getIsInContacts();
        Boolean isBlocking = relationshipsBO.getIsBlocking();

        if (
            Objects.isNull(tgtUserId)
            || Objects.isNull(srcUserId)
        )
            throw new InvalidParameterException("Invalid parameter");

        relationshipsService.saveOrUpdateRelationship(tgtUserId, srcUserId, isFollowing, isInContacts, isBlocking);
    }

    @Override
    public RelationshipsBO getRelationship(RelationshipsBO relationshipsBO) {
        Long tgtUserId = relationshipsBO.getTgtUserId();
        Long srcUserId = relationshipsBO.getSrcUserId();

        if (
            Objects.isNull(tgtUserId)
            || Objects.isNull(srcUserId)
        )
            throw new InvalidParameterException("Invalid parameter");

        return relationshipsService.getRelationship(tgtUserId, srcUserId);
    }
}
