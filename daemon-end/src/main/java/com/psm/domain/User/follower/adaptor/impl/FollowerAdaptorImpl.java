package com.psm.domain.User.follower.adaptor.impl;

import com.psm.domain.User.follower.adaptor.FollowerAdaptor;
import com.psm.domain.User.follower.entity.FollowerBO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.entity.FollowerDTO;
import com.psm.domain.User.follower.types.convertor.FollowerConvertor;
import com.psm.domain.User.follower.service.FollowerService;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.types.utils.Valid.ValidUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Adaptor
public class FollowerAdaptorImpl implements FollowerAdaptor {
    @Autowired
    private ValidUtil validUtil;

    @Autowired
    private FollowerService followerService;

    @Override
    public List<FollowerBO> getByTgtUserId(Long tgtUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId), FollowerDTO.class);

        List<FollowerDAO> followerDAOs = followerService.getByTgtUserId(tgtUserId);

        return followerDAOs.stream().map(FollowerConvertor.INSTANCE::DAO2BO).toList();
    }

    @Override
    public List<FollowerBO> getBySrcUserId(Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("srcUserId", srcUserId), FollowerDTO.class);

        List<FollowerDAO> followerDAOs = followerService.getBySrcUserId(srcUserId);

        return followerDAOs.stream().map(FollowerConvertor.INSTANCE::DAO2BO).toList();
    }

    @Override
    public Long addFollowing(@Valid FollowerDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                || Objects.isNull(followerDTO.getSrcUserId())
                || followerDTO.getTgtUserId().equals(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return followerService.addFollower(followerDTO.getTgtUserId(), followerDTO.getSrcUserId());
    }

    @Override
    public Long addFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), FollowerDTO.class);

        if(tgtUserId.equals(srcUserId)) throw new InvalidParameterException("Invalid parameter");

        return followerService.addFollower(tgtUserId, srcUserId);
    }

    @Override
    public FollowerBO getByTgUserIdAndSrcUserId(@Valid FollowerDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                ||Objects.isNull(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return FollowerConvertor.INSTANCE.DAO2BO(followerService.getByTgUserIdAndSrcUserId(followerDTO.getTgtUserId(), followerDTO.getSrcUserId()));
    }

    @Override
    public FollowerBO getByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), FollowerDTO.class);

        return FollowerConvertor.INSTANCE.DAO2BO(followerService.getByTgUserIdAndSrcUserId(tgtUserId, srcUserId));
    }

    @Override
    public Boolean isFollowed(FollowerDTO followerDTO) {
        return followerService.getByTgUserIdAndSrcUserId(followerDTO.getTgtUserId(), followerDTO.getSrcUserId()) != null;
    }

    @Override
    public Boolean isFollowed(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        return followerService.getByTgUserIdAndSrcUserId(tgtUserId, srcUserId) != null;
    }

    @Override
    public void removeFollowing(@Valid FollowerDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                ||Objects.isNull(followerDTO.getSrcUserId())
                || followerDTO.getTgtUserId().equals(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        followerService.removeByTgUserIdAndSrcUserId(followerDTO.getTgtUserId(), followerDTO.getSrcUserId());
    }

    @Override
    public void removeFollowing(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), FollowerDTO.class);

        if(tgtUserId.equals(srcUserId)) throw new InvalidParameterException("Invalid parameter");

        followerService.removeByTgUserIdAndSrcUserId(tgtUserId, srcUserId);
    }
}
