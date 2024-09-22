package com.psm.domain.User.entity.User;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.psm.domain.User.infrastructure.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class UserDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2388883145401911525L;

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
