package com.psm.domain.User.relationships.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.types.enums.SexEnum;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedUserDO extends UserDO implements Serializable {
    Boolean isFollowed;

    // 全参构造函数
    public ExtendedUserDO(
            Long id,
            String name,
            String password,
            String phone,
            String avatar,
            String email,
            SexEnum sex,
            String profile,
            Short publicModelNum,
            Long modelMaxStorage,
            Long modelCurStorage,
            Boolean isIdle,
            Boolean canUrgent,
            String createTime,
            String modifyTime,
            Boolean deleted,
            Integer version,
            Boolean isFollowed)
    {
        super(
                id,
                name,
                password,
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
                createTime,
                modifyTime,
                deleted,
                version
        );
        this.isFollowed = isFollowed;
    }

    public ExtendedUserDO(UserDO userDO, Boolean followed) {
        super(
                userDO.getId(),
                userDO.getName(),
                userDO.getPassword(),
                userDO.getPhone(),
                userDO.getAvatar(),
                userDO.getEmail(),
                userDO.getSex(),
                userDO.getProfile(),
                userDO.getPublicModelNum(),
                userDO.getModelMaxStorage(),
                userDO.getModelCurStorage(),
                userDO.getIsIdle(),
                userDO.getCanUrgent(),
                userDO.getCreateTime(),
                userDO.getModifyTime(),
                userDO.getDeleted(),
                userDO.getVersion()
        );
        this.isFollowed = followed;
    }

    public static ExtendedUserDO from(UserDO userDO, Boolean followed) {
        return new ExtendedUserDO(userDO, followed);
    }
}
