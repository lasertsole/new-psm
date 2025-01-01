package com.psm.domain.Independent.Model.Joint.models_user.pojo.entity;

import com.psm.domain.Independent.User.Single.user.types.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelUserDO {
    private Long modelId;
    private String title;
    private String cover;
    private String style;
    private String type;
    private Long userId;
    private String name;
    private String avatar;
    private SexEnum sex;
    private String profile;
    private Short publicModelNum;
    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;
}
