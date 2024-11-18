package com.psm.domain.User.user.entity.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO implements Serializable {
    private String id;
    private String name;
    private Boolean hasPass;
    private String phone;
    private String avatar;
    private String email;
    private Boolean sex;
    private String profile;
    private Short publicModelNum;
    private String modelMaxStorage;
    private String modelCurStorage;
    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;
}
