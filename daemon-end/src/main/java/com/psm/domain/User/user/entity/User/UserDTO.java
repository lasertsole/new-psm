package com.psm.domain.User.user.entity.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import com.psm.utils.VO.DTO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable, DTO2VOable<UserVO> {
    private Long id;
    private String name;
    private String password;
    private String changePassword;
    private String phone;
    private String avatar;
    private MultipartFile avatarFile;
    private String oldAvatar;
    private String email;
    private Boolean sex;
    private String profile;
    private Short publicModelNum;
    private Long modelMaxStorage;
    private Long modelCurStorage;
    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;

    public UserDTO(UserBO userBO) {
        BeanUtils.copyProperties(UserConvertor.INSTANCE.BO2DTO(userBO), this);
    }

    public static UserDTO fromBO(UserBO userBO) {
        return UserConvertor.INSTANCE.BO2DTO(userBO);
    }

    @Override
    public UserVO toVO() {
        return UserConvertor.INSTANCE.DTO2OtherVO(this);
    }

    public UserVO toCurrentUserVO() {
        return UserConvertor.INSTANCE.DTO2CurrentVO(this);
    }
}