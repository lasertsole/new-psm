package com.psm.domain.User.user.entity.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import com.psm.types.common.BO.BO;
import com.psm.types.common.DTO.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable, DTO {
    private String id;
    private String name;
    private String password;
    private String changePassword;
    private Boolean hasPass;
    private String phone;
    private String avatar;
    private MultipartFile avatarFile;
    private String oldAvatar;
    private String email;
    private Boolean sex;
    private String profile;
    private Short publicModelNum;
    private String modelMaxStorage;
    private String modelCurStorage;
    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;
    public static UserDTO currentFromBO(UserBO userBO) {
        return UserConvertor.INSTANCE.BO2CurrentDTO(userBO);
    }

    public static UserDTO otherFromBO(UserBO userBO) {
        return UserConvertor.INSTANCE.BO2OtherDTO(userBO);
    }

    @Override
    public BO toBO() {
        return UserConvertor.INSTANCE.DTO2BO(this);
    }
}