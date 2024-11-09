package com.psm.domain.Model.models_user.entity;

import com.psm.domain.User.user.types.enums.SexEnum;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
public class ModelUserDAO {
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
