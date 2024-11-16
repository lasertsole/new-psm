package com.psm.domain.User.relationships.adaptor.impl;

import com.psm.domain.User.relationships.adaptor.RelationshipsAdaptor;
import com.psm.domain.User.relationships.entity.RelationshipsBO;
import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.domain.User.relationships.entity.RelationshipsDTO;
import com.psm.domain.User.relationships.types.convertor.RelationshipsConvertor;
import com.psm.domain.User.relationships.service.RelationshipsService;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.utils.Valid.ValidUtil;
import jakarta.validation.Valid;
import org.apache.rocketmq.shaded.io.grpc.netty.shaded.io.netty.handler.codec.serialization.ObjectEncoder;
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

        List<RelationshipsDAO> followerDAOs = relationshipsService.checkFollowers(tgtUserId);

        return followerDAOs.stream().map(RelationshipsConvertor.INSTANCE::DAO2BO).toList();
    }

    @Override
    public List<RelationshipsBO> checkFollowing(Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("srcUserId", srcUserId), RelationshipsDTO.class);

        List<RelationshipsDAO> followerDAOs = relationshipsService.checkFollowing(srcUserId);

        return followerDAOs.stream().map(RelationshipsConvertor.INSTANCE::DAO2BO).toList();
    }

    @Override
    public void addFollowing(@Valid RelationshipsDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                || Objects.isNull(followerDTO.getSrcUserId())
                || followerDTO.getTgtUserId().equals(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        relationshipsService.following(followerDTO.getTgtUserId(), followerDTO.getSrcUserId());
    }

    @Override
    public void addFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), RelationshipsDTO.class);

        if(tgtUserId.equals(srcUserId)) throw new InvalidParameterException("Invalid parameter");

        relationshipsService.following(tgtUserId, srcUserId);
    }

    @Override
    public RelationshipsBO checkFollowShip(@Valid RelationshipsDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                ||Objects.isNull(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return RelationshipsConvertor.INSTANCE.DAO2BO(relationshipsService.checkFollowShip(followerDTO.getTgtUserId(), followerDTO.getSrcUserId()));
    }

    @Override
    public RelationshipsBO checkFollowShip(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), RelationshipsDTO.class);

        return RelationshipsConvertor.INSTANCE.DAO2BO(relationshipsService.checkFollowShip(tgtUserId, srcUserId));
    }

    @Override
    public Boolean isFollowed(@Valid RelationshipsDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                || Objects.isNull(followerDTO.getSrcUserId())
                || followerDTO.getTgtUserId().equals(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return Objects.nonNull(relationshipsService.checkFollowShip(followerDTO.getTgtUserId(), followerDTO.getSrcUserId()));
    }

    @Override
    public Boolean isFollowed(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        return Objects.nonNull(relationshipsService.checkFollowShip(tgtUserId, srcUserId));
    }

    @Override
    public void removeFollowing(@Valid RelationshipsDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                ||Objects.isNull(followerDTO.getSrcUserId())
                || followerDTO.getTgtUserId().equals(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        relationshipsService.unFollowing(followerDTO.getTgtUserId(), followerDTO.getSrcUserId());
    }

    @Override
    public void removeFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), RelationshipsDTO.class);

        if(tgtUserId.equals(srcUserId)) throw new InvalidParameterException("Invalid parameter");

        relationshipsService.unFollowing(tgtUserId, srcUserId);
    }

    @Override
    public void saveOrUpdateRelationship(@Valid RelationshipsDTO relationshipsDTO) {
        if (
            Objects.isNull(relationshipsDTO.getId())
            && (Objects.isNull(relationshipsDTO.getTgtUserId())
            || Objects.isNull(relationshipsDTO.getSrcUserId()))
        )
            throw new InvalidParameterException("Invalid parameter");

        relationshipsService.saveOrUpdateRelationship(RelationshipsConvertor.INSTANCE.DTO2DAO(relationshipsDTO));
    }

    @Override
    public RelationshipsBO getRelationship(RelationshipsDTO relationshipsDTO) {
        if (
            Objects.isNull(relationshipsDTO.getId())
            && (Objects.isNull(relationshipsDTO.getTgtUserId())
            || Objects.isNull(relationshipsDTO.getSrcUserId()))
        )
            throw new InvalidParameterException("Invalid parameter");

        RelationshipsDAO relationship = relationshipsService.getRelationship(RelationshipsConvertor.INSTANCE.DTO2DAO(relationshipsDTO));
        return RelationshipsConvertor.INSTANCE.DAO2BO(relationship);
    }
}
