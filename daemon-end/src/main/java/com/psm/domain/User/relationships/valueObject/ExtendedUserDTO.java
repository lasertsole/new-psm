package com.psm.domain.User.relationships.valueObject;

import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.domain.User.relationships.types.convertor.ExtendedUserConvertor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExtendedUserDTO extends UserDTO implements Serializable {
    Boolean isFollowed;

    // 全参构造函数
    public ExtendedUserDTO(
        Long id,
        String name,
        String password,
        String phone,
        String avatar,
        String email,
        Boolean sex,
        String profile,
        Short publicModelNum,
        Long modelMaxStorage,
        Long modelCurStorage,
        Boolean isIdle,
        Boolean canUrgent,
        String createTime,
        Boolean isFollowed)
    {
        super(
            id,
            name,
            password,
            null,
            phone,
            avatar,
            null,
            null,
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
        this.isFollowed = isFollowed;
    }

    public ExtendedUserDTO(UserDTO userDTO, Boolean followed) {
        super(
            userDTO.getId(),
            userDTO.getName(),
            userDTO.getPassword(),
            null,
            userDTO.getPhone(),
            userDTO.getAvatar(),
            null,
            null,
            userDTO.getEmail(),
            userDTO.getSex(),
            userDTO.getProfile(),
            userDTO.getPublicModelNum(),
            userDTO.getModelMaxStorage(),
            userDTO.getModelCurStorage(),
            userDTO.getIsIdle(),
            userDTO.getCanUrgent(),
            userDTO.getCreateTime()
        );
        this.isFollowed = followed;
    }

    public static ExtendedUserDTO from(UserDTO userDTO, Boolean followed) {
        return new ExtendedUserDTO(userDTO, followed);
    }

    @Override
    public ExtendedUserVO toVO() {
        return ExtendedUserConvertor.INSTANCE.DTO2VO(this);
    }
}
