package com.psm.domain.User.user.entity.User.UserVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1255229981356906416L;

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
