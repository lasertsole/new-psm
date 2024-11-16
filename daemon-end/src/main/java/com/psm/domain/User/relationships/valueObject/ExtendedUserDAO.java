package com.psm.domain.User.relationships.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.types.enums.SexEnum;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedUserDAO extends UserDAO implements Serializable {
    Boolean isFollowed;

    // 全参构造函数
    public ExtendedUserDAO(
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

    public ExtendedUserDAO(UserDAO userDAO, Boolean followed) {
        super(
                userDAO.getId(),
                userDAO.getName(),
                userDAO.getPassword(),
                userDAO.getPhone(),
                userDAO.getAvatar(),
                userDAO.getEmail(),
                userDAO.getSex(),
                userDAO.getProfile(),
                userDAO.getPublicModelNum(),
                userDAO.getModelMaxStorage(),
                userDAO.getModelCurStorage(),
                userDAO.getIsIdle(),
                userDAO.getCanUrgent(),
                userDAO.getCreateTime(),
                userDAO.getModifyTime(),
                userDAO.getDeleted(),
                userDAO.getVersion()
        );
        this.isFollowed = followed;
    }

    public static ExtendedUserDAO from(UserDAO userDAO, Boolean followed) {
        return new ExtendedUserDAO(userDAO, followed);
    }
}
