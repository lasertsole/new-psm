package com.psm.domain.User.entity.User.UserVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6710910285903195194L;

    private String id;
    private String name;
    private Boolean hasPass;
    private String phone;
    private String avatar;
    private String email;
    private Boolean sex;
    private String profile;
    private String createTime;
}
