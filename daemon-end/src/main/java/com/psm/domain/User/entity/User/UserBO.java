package com.psm.domain.User.entity.User;

import com.psm.domain.User.entity.User.UserVO.CurrentUserVO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import com.psm.domain.User.infrastructure.Convertor.UserConvertor;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBO implements BO2VOable<OtherUserVO>, Serializable {
    @Serial
    private static final long serialVersionUID = 734750268487283107L;

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
    public OtherUserVO toVO() {
        return UserConvertor.INSTANCE.BO2VO(this);
    }

    public CurrentUserVO toCurrentUserVO() {
        return UserConvertor.INSTANCE.BO2CurrentVO(this);
    }
}
