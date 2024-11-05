package com.psm.domain.User.user.entity.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import com.psm.types.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBO implements BO2VOable<UserVO>, Serializable {
    @Serial
    private static final long serialVersionUID = -5229929153741804460L;

    private Long id;
    private String name;
    private String password;
    private String phone;
    private String avatar;
    private String email;
    private Boolean sex;
    private String profile;
    private String createTime;
    private String modifyTime;

    @Override
    public UserVO toVO() {
        return UserConvertor.INSTANCE.BO2OtherVO(this);
    }

    public UserVO toCurrentUserVO() {
        return UserConvertor.INSTANCE.BO2CurrentVO(this);
    }
}
