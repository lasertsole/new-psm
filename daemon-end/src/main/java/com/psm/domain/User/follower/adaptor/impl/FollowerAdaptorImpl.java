package com.psm.domain.User.follower.adaptor.impl;

import com.psm.domain.User.follower.adaptor.FollowerAdaptor;
import com.psm.domain.User.follower.entity.FollowerBO;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.entity.FollowerDTO;
import com.psm.domain.User.follower.infrastructure.convertor.FollowerConvertor;
import com.psm.domain.User.follower.service.FollowerService;
import com.psm.infrastructure.annotation.spring.Adaptor;
import com.psm.infrastructure.utils.Valid.ValidUtil;
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
    public Long addFollower(@Valid FollowerDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                ||Objects.isNull(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return followerService.addFollower(followerDTO.getTgtUserId(), followerDTO.getSrcUserId());
    }

    @Override
    public Long addFollower(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), FollowerDTO.class);

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
    public void removeByTgUserIdAndSrcUserId(@Valid FollowerDTO followerDTO) {
        if(
                Objects.isNull(followerDTO.getTgtUserId())
                ||Objects.isNull(followerDTO.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        followerService.removeByTgUserIdAndSrcUserId(followerDTO.getTgtUserId(), followerDTO.getSrcUserId());
    }

    @Override
    public void removeByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) throws InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("tgtUserId", tgtUserId, "srcUserId", srcUserId), FollowerDTO.class);

        followerService.removeByTgUserIdAndSrcUserId(tgtUserId, srcUserId);
    }
}
