package com.psm.domain.User.relationships.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.relationships.types.convertor.ExtendedUserConvertor;
import com.psm.domain.User.user.entity.User.UserVO;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedUserVO extends UserVO implements Serializable {
    Boolean isFollowed;

    // 全参构造函数
    public ExtendedUserVO(
        String id,
        String name,
        Boolean hasPass,
        String phone,
        String avatar,
        String email,
        Boolean sex,
        String profile,
        Short publicModelNum,
        String modelMaxStorage,
        String modelCurStorage,
        Boolean isIdle,
        Boolean canUrgent,
        String createTime,
        Boolean followed)
    {
        super(
            id,
            name,
            hasPass,
            phone,
            avatar,
            email,
            sex,
            profile,
            publicModelNum,
            modelMaxStorage,
            modelCurStorage,
            isIdle,
            canUrgent,
            createTime
        );
        this.isFollowed = followed;
    }

    public ExtendedUserVO(UserVO userVO, Boolean followed) {
        super(
            userVO.getId().toString(),
            userVO.getName(),
            null,
            userVO.getPhone(),
            userVO.getAvatar(),
            userVO.getEmail(),
            userVO.getSex(),
            userVO.getProfile(),
            userVO.getPublicModelNum(),
            userVO.getModelMaxStorage(),
            userVO.getModelCurStorage(),
            userVO.getIsIdle(),
            userVO.getCanUrgent(),
            userVO.getCreateTime()
        );
        this.isFollowed = followed;
    }
}
