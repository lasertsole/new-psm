package com.psm.domain.User.entity.User;

import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBO implements BO2VOable<UserVO> {
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
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(this, userVO);
        return userVO;
    }
}
