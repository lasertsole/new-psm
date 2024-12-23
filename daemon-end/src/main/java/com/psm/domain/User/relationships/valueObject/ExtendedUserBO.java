package com.psm.domain.User.relationships.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.types.enums.SexEnum;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedUserBO extends UserBO implements Serializable {
    Boolean isFollowed;

    // 全参构造函数
    public ExtendedUserBO(
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
        Boolean isFollowed)
    {
        super(
            null,
            id,
            name,
            password,
            null,
            phone,
            null,
            avatar,
            null,
            email,
            sex,
            profile,
            publicModelNum,
            modelMaxStorage,
            modelCurStorage,
            isIdle,
            canUrgent,
            createTime,
            modifyTime
        );
        this.isFollowed = isFollowed;
    }

    public ExtendedUserBO(UserBO userBO, Boolean followed) {
        super(
            null,
            userBO.getId(),
            userBO.getName(),
            userBO.getPassword(),
            null,
            userBO.getPhone(),
            null,
            userBO.getAvatar(),
            null,
            userBO.getEmail(),
            userBO.getSex(),
            userBO.getProfile(),
            userBO.getPublicModelNum(),
            userBO.getModelMaxStorage(),
            userBO.getModelCurStorage(),
            userBO.getIsIdle(),
            userBO.getCanUrgent(),
            userBO.getCreateTime(),
            userBO.getModifyTime()
        );
        this.isFollowed = followed;
    }

    public static ExtendedUserBO from(UserBO userBO, Boolean followed) {
        return new ExtendedUserBO(userBO, followed);
    }
}
