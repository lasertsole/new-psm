package com.psm.domain.User.follower.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.follower.types.convertor.ExtendedUserConvertor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.types.enums.SexEnum;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedUserBO extends UserBO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2293374811762963476L;

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
        String createTime,
        String modifyTime,
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
            createTime,
            modifyTime
        );
        this.isFollowed = isFollowed;
    }

    public ExtendedUserBO(UserBO userBO, Boolean followed) {
        super(
            userBO.getId(),
            userBO.getName(),
            userBO.getPassword(),
            userBO.getPhone(),
            userBO.getAvatar(),
            userBO.getEmail(),
            userBO.getSex(),
            userBO.getProfile(),
            userBO.getCreateTime(),
            userBO.getModifyTime()
        );
        this.isFollowed = followed;
    }

    public static ExtendedUserBO from(UserBO userBO, Boolean followed) {
        return new ExtendedUserBO(userBO, followed);
    }

    @Override
    public ExtendedUserVO toVO() {
        return ExtendedUserConvertor.INSTANCE.BO2OtherVO(this);
    }
}
