package com.psm.domain.User.user.entity.User;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.types.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8551539412702599762L;

    @TableId
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String avatar;
    private String email;
    private SexEnum sex;
    private String profile;
    private Short publicModelNum;
    private Long modelMaxStorage;
    private Long modelCurStorage;
    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;
    private String modifyTime;

    @TableLogic
    private Boolean deleted;

    @Version
    private Integer version;
}
