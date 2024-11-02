package com.psm.domain.User.user.entity.User;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.infrastructure.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1869498695200374998L;

    @TableId
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String avatar;
    private String email;
    private SexEnum sex;
    private String profile;
    private String createTime;
    private String modifyTime;

    @TableLogic
    private Boolean deleted;

    @Version
    private Integer version;
}
